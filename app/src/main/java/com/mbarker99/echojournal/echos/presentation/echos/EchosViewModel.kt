package com.mbarker99.echojournal.echos.presentation.echos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbarker99.echojournal.R
import com.mbarker99.echojournal.core.presentation.designsystem.dropdowns.Selectable
import com.mbarker99.echojournal.core.presentation.util.UiText
import com.mbarker99.echojournal.echos.domain.recording.VoiceRecorder
import com.mbarker99.echojournal.echos.presentation.echos.model.AudioCaptureMethod
import com.mbarker99.echojournal.echos.presentation.echos.model.EchoFilterChip
import com.mbarker99.echojournal.echos.presentation.echos.model.MoodChipContent
import com.mbarker99.echojournal.echos.presentation.echos.model.RecordingState
import com.mbarker99.echojournal.echos.presentation.model.MoodUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

class EchosViewModel(
    private val voiceRecorder: VoiceRecorder
) : ViewModel() {

    companion object {
        private val MIN_RECORD_DURATION = 1.5.seconds
    }
    private var hasLoadedInitialData = false

    private val selectedMoodFilters = MutableStateFlow<List<MoodUi>>(emptyList())
    private val selectedTopicFilters = MutableStateFlow<List<String>>(emptyList())

    private val eventChannel = Channel<EchosEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(EchosState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeFilters()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchosState()
        )

    fun onAction(action: EchosAction) {
        when (action) {
            EchosAction.OnFabClick -> {
                requestAudioPermission()
                _state.update { it.copy(
                    currentCaptureMethod = AudioCaptureMethod.STANDARD
                ) }
            }

            EchosAction.OnFabLongPress -> {
                requestAudioPermission()
                _state.update { it.copy(
                    currentCaptureMethod = AudioCaptureMethod.QUICK
                ) }
            }

            EchosAction.OnSettingsClick -> {}

            EchosAction.OnMoodChipClick -> {
                _state.update {
                    it.copy(
                        selectedEchoFilterChip = EchoFilterChip.MOODS
                    )
                }
            }
            is EchosAction.OnRemoveFilters -> {
                when (action.filterType) {
                    EchoFilterChip.MOODS -> selectedMoodFilters.update { emptyList() }
                    EchoFilterChip.TOPICS -> selectedTopicFilters.update { emptyList() }
                }
            }
            EchosAction.OnTopicChipClicked -> {
                _state.update {
                     it.copy(
                         selectedEchoFilterChip = EchoFilterChip.TOPICS
                     )
                }
            }

            EchosAction.OnDismissTopicDropDown,
            EchosAction.OnDismissMoodDropDown -> {
                _state.update { it.copy(selectedEchoFilterChip = null) }
            }

            is EchosAction.OnFilterByMoodClick -> {
                toggleMoodFilter(action.moodUi)
            }

            is EchosAction.OnFilterByTopicClick -> {
               toggleTopicFilter(action.topic)
            }

            EchosAction.OnPauseEchoClick -> {}
            is EchosAction.OnPlayEchoClick -> {}
            is EchosAction.OnTrackSizeAvailable -> {}
            is EchosAction.OnAudioPermissionGranted -> {
                startRecording(captureMethod = AudioCaptureMethod.STANDARD)
            }

            // Recording
            EchosAction.OnCancelRecordingClick -> cancelRecording()
            EchosAction.OnCompleteRecordingClick -> stopRecording()
            EchosAction.OnPauseRecordingClick -> pauseRecording()
            EchosAction.OnResumeRecordingClick -> resumeRecording()
        }
    }

    private fun requestAudioPermission() = viewModelScope.launch {
        eventChannel.send(EchosEvent.RequestAudioPermission)
    }

    private fun pauseRecording() {
        voiceRecorder.pause()
        _state.update {
            it.copy(
                recordingState = RecordingState.PAUSED
            )
        }
    }

    private fun resumeRecording() {
        voiceRecorder.resume()
        _state.update {
            it.copy(
                recordingState = RecordingState.NORMAL_CAPTURE
            )
        }
    }

    private fun cancelRecording() {
        _state.update {
            it.copy(
                recordingState = RecordingState.NOT_RECORDING,
                currentCaptureMethod = null
            )
        }
        voiceRecorder.cancel()
    }

    private fun stopRecording() {
        voiceRecorder.stop()
        _state.update {
            it.copy(
                recordingState = RecordingState.NOT_RECORDING,
                currentCaptureMethod = null
            )
        }
        val recordingDetails = voiceRecorder.recordingDetails.value
        viewModelScope.launch {
            if (recordingDetails.duration < MIN_RECORD_DURATION) {
                eventChannel.send(EchosEvent.RecordingTooShort)
            } else {
                eventChannel.send(EchosEvent.OnCompleteRecording)
            }
        }
    }

    private fun startRecording(captureMethod: AudioCaptureMethod) {
        _state.update {
            it.copy(
                recordingState = when (captureMethod) {
                    AudioCaptureMethod.STANDARD -> RecordingState.NORMAL_CAPTURE
                    AudioCaptureMethod.QUICK -> RecordingState.QUICK_CAPTURE
                }
            )
        }

        voiceRecorder.start()
        if (captureMethod == AudioCaptureMethod.STANDARD) {
            voiceRecorder
                .recordingDetails
                .distinctUntilChangedBy { it.duration }
                .map { it.duration }
                .onEach { duration ->
                    _state.update {
                        it.copy(
                            recordingElapsedDuration = duration
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun toggleMoodFilter(moodUi: MoodUi) {
        selectedMoodFilters.update { selectedMoods ->
            if (moodUi in selectedMoods) {
                selectedMoods - moodUi
            } else  {
                selectedMoods + moodUi
            }
        }
    }

    private fun toggleTopicFilter(topic: String) {
        selectedTopicFilters.update { selectedTopics ->
            if (topic in selectedTopics) {
                selectedTopics - topic
            } else  {
                selectedTopics + topic
            }
        }
    }

    private fun observeFilters() {
        combine(
            selectedTopicFilters,
            selectedMoodFilters
        ) { selectedTopics, selectedMoods ->
            _state.update { it.copy(
                topics = it.topics.map{ selectableTopic ->
                    Selectable(
                        item = selectableTopic.item,
                        selected = selectedTopics.contains(selectableTopic.item)
                    )
                },
                moods = MoodUi.entries.map { moodUi ->
                    Selectable(
                        item = moodUi,
                        selected = selectedMoods.contains(moodUi)
                    )
                },
                hasActiveMoodFilters = selectedMoods.isNotEmpty(),
                hasActiveTopicFilters = selectedTopics.isNotEmpty(),
                topicChipTitle = selectedTopics.asTopicChipContent(),
                moodChipContent = selectedMoods.asMoodChipContent()
            ) }
        }.launchIn(viewModelScope)
    }

    private fun List<String>.asTopicChipContent() : UiText {
        return when (size) {
            0 -> UiText.StringResource(R.string.all_topics)
            1 -> UiText.Dynamic(this.first())
            2 -> UiText.Dynamic("${this.first()}, ${this.last()}")
            else -> {
                val extraElementCount = size - 2
                UiText.Dynamic("${this.first()}, ${this[1]} +$extraElementCount")
            }
        }
    }

    private fun List<MoodUi>.asMoodChipContent(): MoodChipContent {
        if (this.isEmpty()) {
            return MoodChipContent()
        }

        val icons = this.map { it.iconSet.fill }
        val moodNames = this.map { it.title }

        return when(size) {
            1 -> MoodChipContent(
                iconsRes = icons,
                title = moodNames.first()
            )
            2 -> MoodChipContent(
                iconsRes = icons,
                title = UiText.Combined(
                    format = "%s, %s",
                    uiTexts = moodNames.toTypedArray()
                )
            )
            else -> {
                val extraElementCount = size - 2
                MoodChipContent(
                    iconsRes = icons,
                    title = UiText.Combined(
                        format = "%s, %s +$extraElementCount",
                        uiTexts = moodNames.take(2).toTypedArray()
                    )
                )
            }
        }
    }
}