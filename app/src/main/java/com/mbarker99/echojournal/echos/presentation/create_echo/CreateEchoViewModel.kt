@file:OptIn(FlowPreview::class)

package com.mbarker99.echojournal.echos.presentation.create_echo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mbarker99.echojournal.app.navigation.Route
import com.mbarker99.echojournal.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import com.mbarker99.echojournal.echos.domain.recording.RecordingStorage
import com.mbarker99.echojournal.echos.presentation.echos.model.TrackSizeInfo
import com.mbarker99.echojournal.echos.presentation.echos.util.AmplitudeNormalizer
import com.mbarker99.echojournal.echos.presentation.echos.util.toRecordingDetails
import com.mbarker99.echojournal.echos.presentation.model.MoodUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEchoViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val recordingStorage: RecordingStorage
): ViewModel() {

    private var hasLoadedInitialData = false

    private val route = savedStateHandle.toRoute<Route.CreateEcho>()
    private val recordingDetails = route.toRecordingDetails()

    private val eventChannel = Channel<CreateEchoEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(CreateEchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeAddTopicText()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateEchoState()
        )

    private fun observeAddTopicText() {
        state
            .map { it.addTopicText }
            .distinctUntilChanged()
            .debounce(300)
            .onEach { query ->
                _state.update {
                    it.copy(
                        showTopicSuggestions = query.isNotBlank() && query.trim() !in it.topics,
                        searchResults = listOf("hello", "helloworld").asUnselectedItems()
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
            is CreateEchoAction.OnNoteTextChanged -> { }
            CreateEchoAction.OnPauseAudioClick -> {}
            CreateEchoAction.OnPlayAudioClick -> {}
            CreateEchoAction.OnSaveClick -> onSaveClick()
            is CreateEchoAction.OnTitleTextChanged -> onTitleTextChanged(action.text)
            is CreateEchoAction.OnTrackSizeAvailable -> onTrackSizeAvailable(action.trackSizeInfo)
            CreateEchoAction.OnCancelClick,
            CreateEchoAction.OnNavigateBackClick,
            CreateEchoAction.OnNavigateBack -> onShowConfirmLeaveDialog()
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
                titleText = text
            )
        }
    }

    private fun onSaveClick() {
        if (recordingDetails.filePath == null) {
            return
        }

        viewModelScope.launch {
            val savedFilePath =  recordingStorage.savePersistently(
                tempFilePath = recordingDetails.filePath
            )
            if (savedFilePath == null) {
                eventChannel.send(CreateEchoEvent.FailedToSaveFile)
                return@launch
            }

            // TODO : Echo
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