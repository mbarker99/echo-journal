@file:OptIn(FlowPreview::class)

package com.mbarker99.echojournal.echos.presentation.create_echo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import com.mbarker99.echojournal.app.navigation.Route
import com.mbarker99.echojournal.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import com.mbarker99.echojournal.echos.domain.audio.AudioPlayer
import com.mbarker99.echojournal.echos.domain.echo.EchoDataSource
import com.mbarker99.echojournal.echos.domain.echo.model.Echo
import com.mbarker99.echojournal.echos.domain.echo.model.Mood
import com.mbarker99.echojournal.echos.domain.recording.RecordingStorage
import com.mbarker99.echojournal.echos.domain.settings.SettingsPreferences
import com.mbarker99.echojournal.echos.presentation.echos.model.PlaybackState
import com.mbarker99.echojournal.echos.presentation.echos.model.TrackSizeInfo
import com.mbarker99.echojournal.echos.presentation.echos.util.AmplitudeNormalizer
import com.mbarker99.echojournal.echos.presentation.echos.util.toRecordingDetails
import com.mbarker99.echojournal.echos.presentation.model.MoodUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.collections.map
import kotlin.text.contains
import kotlin.time.Duration

class CreateEchoViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val recordingStorage: RecordingStorage,
    private val audioPlayer: AudioPlayer,
    private val echoDataSource: EchoDataSource,
    private val settingsPreferences: SettingsPreferences
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val route = savedStateHandle.toRoute<Route.CreateEcho>()
    private val recordingDetails = route.toRecordingDetails()

    private val eventChannel = Channel<CreateEchoEvent>()
    val events = eventChannel.receiveAsFlow()

    private val restoredTopics = savedStateHandle.get<String>("topics")?.split(",")
    private val _state = MutableStateFlow(
        CreateEchoState(
            playbackTotalDuration = recordingDetails.duration,
            titleText = savedStateHandle["titleText"] ?: "",
            noteText = savedStateHandle["noteText"] ?: "",
            topics = restoredTopics ?: emptyList(),
            mood = savedStateHandle.get<String>("mood")?.let {
                MoodUi.valueOf(it)
            },
            showMoodSelector = savedStateHandle.get<String>("mood") == null,
            canSaveEcho = savedStateHandle.get<Boolean>("canSaveEcho") == true
        )
    )
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeAddTopicText()
                fetchDefaultSettings()
                observeSearchResults()
                hasLoadedInitialData = true
            }
        }
        .onEach { state ->
            savedStateHandle["titleText"] = state.titleText
            savedStateHandle["noteText"] = state.noteText
            savedStateHandle["topics"] = state.topics.joinToString(",")
            savedStateHandle["mood"] = state.mood?.name
            savedStateHandle["canSaveEcho"] = state.canSaveEcho
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateEchoState()
        )

    private var durationJob: Job? = null

    private fun fetchDefaultSettings() {
        settingsPreferences
            .observeDefaultMood()
            .take(1)
            .onEach { defaultMood ->
                val mood = MoodUi.valueOf(defaultMood.name)
                _state.update {
                    it.copy(
                        selectedMood = mood,
                        mood = mood,
                        showMoodSelector = false
                    )
                }
            }
            .launchIn(viewModelScope)

        settingsPreferences
            .observeDefaultTopics()
            .take(1)
            .onEach { defaultTopics ->
                _state.update {
                    it.copy(
                        topics = defaultTopics
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSearchResults() {
        state
            .distinctUntilChangedBy { it.addTopicText }
            .map { it.addTopicText }
            .debounce(300)
            .flatMapLatest { query ->
                if (query.isNotBlank()) {
                    echoDataSource.searchTopics(query)
                } else emptyFlow()
            }
            .onEach { filteredResults ->
                _state.update {
                    val searchText = it.addTopicText.trim()
                    val isNewTopic = searchText !in filteredResults && searchText !in it.topics
                            && searchText.isNotBlank()
                    it.copy(
                        searchResults = filteredResults.asUnselectedItems(),
                        showTopicSuggestions = filteredResults.isNotEmpty() || isNewTopic,
                        showCreateTopicOption = isNewTopic
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeAddTopicText() {
        state
            .map { it.addTopicText }
            .distinctUntilChanged()
            .debounce(300)
            .onEach { query ->
                _state.update {
                    it.copy(
                        showTopicSuggestions = query.isNotBlank() && query.trim() !in it.topics,
                        searchResults = it.searchResults
                    )
                }
            }
            .launchIn(viewModelScope)
    }


    fun onAction(action: CreateEchoAction) {
        when (action) {
            CreateEchoAction.OnDismissMoodSelector -> onDismissMoodSelector()
            CreateEchoAction.OnSelectMoodClick -> onSelectMoodClick()
            is CreateEchoAction.OnMoodClick -> onMoodClick(action.mood)
            CreateEchoAction.OnConfirmMood -> onConfirmMood()
            is CreateEchoAction.OnAddTopicTextChanged -> onAddTopicTextChanged(action.text)
            is CreateEchoAction.OnTopicClick -> onTopicClick(action.topic)
            is CreateEchoAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            CreateEchoAction.OnDismissTopicSuggestions -> onDismissTopicSuggestions()
            CreateEchoAction.OnDismissConfirmLeaveDialog -> onDismissConfirmLeaveDialog()
            is CreateEchoAction.OnNoteTextChanged -> onNoteTextChanged(action.text)
            CreateEchoAction.OnPauseAudioClick -> audioPlayer.pause()
            CreateEchoAction.OnPlayAudioClick -> onPlayAudioClick()
            CreateEchoAction.OnSaveClick -> onSaveClick()
            is CreateEchoAction.OnTitleTextChanged -> onTitleTextChanged(action.text)
            is CreateEchoAction.OnTrackSizeAvailable -> onTrackSizeAvailable(action.trackSizeInfo)
            CreateEchoAction.OnCancelClick,
            CreateEchoAction.OnNavigateBackClick,
            CreateEchoAction.OnNavigateBack -> onShowConfirmLeaveDialog()
        }
    }

    private fun onNoteTextChanged(text: String) {
        _state.update {
            it.copy(
                noteText = text
            )
        }
    }

    private fun onPlayAudioClick() {
        if (state.value.playbackState == PlaybackState.PAUSED) {
            audioPlayer.resume()
        } else {
            audioPlayer.play(
                filePath = recordingDetails.filePath ?: throw IllegalArgumentException(
                    "File path cannot be null"
                ),
                onComplete = {
                    _state.update {
                        it.copy(
                            playbackState = PlaybackState.STOPPED,
                            durationPlayed = Duration.ZERO
                        )
                    }
                }
            )

            durationJob = audioPlayer
                .activeTrack
                .filterNotNull()
                .onEach { track ->
                    _state.update {
                        it.copy(
                            playbackState = if (track.isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED,
                            durationPlayed = track.durationPlayed
                        )
                    }
                }
                .launchIn(viewModelScope)

        }
    }

    private fun onTrackSizeAvailable(trackSizeInfo: TrackSizeInfo) {
        viewModelScope.launch(Dispatchers.Default) {
            val finalAmplitudes = AmplitudeNormalizer.normalize(
                sourceAmplitudes = recordingDetails.amplitudes,
                trackWidth = trackSizeInfo.trackWidth,
                barWidth = trackSizeInfo.barWidth,
                spacing = trackSizeInfo.spacing,
            )

            _state.update {
                it.copy(
                    playbackAmplitudes = finalAmplitudes
                )
            }
        }
    }

    private fun onTitleTextChanged(text: String) {
        _state.update {
            it.copy(
                titleText = text,
                canSaveEcho = text.isNotBlank() && it.mood != null
            )
        }
    }

    private fun onSaveClick() {
        if (recordingDetails.filePath == null || !state.value.canSaveEcho) {
            return
        }

        viewModelScope.launch {
            val savedFilePath = recordingStorage.savePersistently(
                tempFilePath = recordingDetails.filePath
            )
            if (savedFilePath == null) {
                eventChannel.send(CreateEchoEvent.FailedToSaveFile)
                return@launch
            }

            val currentState = state.value
            val echo = Echo(
                mood = currentState.mood?.let {
                    Mood.valueOf(it.name)
                } ?: throw IllegalStateException("Mood must be set before saving echo"),
                title = currentState.titleText.trim(),
                note = currentState.noteText.ifBlank { null },
                topics = currentState.topics,
                audioFilePath = savedFilePath,
                audioPlaybackLength = currentState.playbackTotalDuration,
                audioAmplitudes = recordingDetails.amplitudes,
                recordedAt = Instant.now()
            )
            echoDataSource.insertEcho(echo)
            eventChannel.send(CreateEchoEvent.EchoSuccessfullySaved)
        }

    }

    private fun onShowConfirmLeaveDialog() {
        _state.update {
            it.copy(
                showConfirmLeaveDialog = true
            )
        }
    }

    private fun onDismissConfirmLeaveDialog() {
        _state.update {
            it.copy(
                showConfirmLeaveDialog = false
            )
        }
    }

    private fun onDismissTopicSuggestions() {
        _state.update {
            it.copy(
                showTopicSuggestions = false
            )
        }
    }

    private fun onRemoveTopicClick(topic: String) {
        _state.update {
            it.copy(
                topics = it.topics - topic
            )
        }
    }

    private fun onTopicClick(topic: String) {
        _state.update {
            it.copy(
                addTopicText = "",
                topics = (it.topics + topic).distinct()
            )
        }
    }

    private fun onAddTopicTextChanged(text: String) {
        _state.update {
            it.copy(
                addTopicText = text.filter {
                    it.isLetterOrDigit()
                }
            )
        }
    }

    private fun onSelectMoodClick() {
        _state.update {
            it.copy(
                showMoodSelector = true
            )
        }
    }

    private fun onMoodClick(mood: MoodUi) {
        _state.update {
            it.copy(
                selectedMood = mood
            )
        }
    }

    private fun onDismissMoodSelector() {
        _state.update {
            it.copy(
                showMoodSelector = false
            )
        }
    }

    private fun onConfirmMood() {
        _state.update {
            it.copy(
                mood = it.selectedMood,
                canSaveEcho = it.titleText.isNotBlank(),
                showMoodSelector = false
            )
        }
    }
}