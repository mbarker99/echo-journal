package com.mbarker99.echojournal.echos.presentation.create_echo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbarker99.echojournal.echos.presentation.echos.EchosAction
import com.mbarker99.echojournal.echos.presentation.echos.EchosState
import com.mbarker99.echojournal.echos.presentation.model.MoodUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CreateEchoViewModel: ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CreateEchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateEchoState()
        )

    fun onAction(action: CreateEchoAction) {
        when (action) {
            CreateEchoAction.OnDismissMoodSelector -> onDismissMoodSelector()
            CreateEchoAction.OnSelectMoodClick -> onSelectMoodClick()
            is CreateEchoAction.OnMoodClick -> onMoodClick(action.mood)
            CreateEchoAction.OnConfirmMood -> onConfirmMood()
            is CreateEchoAction.OnAddTopicTextChanged -> TODO()
            CreateEchoAction.OnCancelClick -> TODO()
            CreateEchoAction.OnCreateNewTopicClick -> TODO()
            CreateEchoAction.OnDismissTopicSuggestions -> TODO()
            CreateEchoAction.OnNavigateBackClick -> TODO()
            is CreateEchoAction.OnNoteTextChanged -> TODO()
            CreateEchoAction.OnPauseAudioClick -> TODO()
            CreateEchoAction.OnPlayAudioClick -> TODO()
            is CreateEchoAction.OnRemoveTopicClick -> TODO()
            CreateEchoAction.OnSaveClick -> TODO()
            is CreateEchoAction.OnTitleTextChanged -> TODO()
            is CreateEchoAction.OnTopicClick -> TODO()
            is CreateEchoAction.OnTrackSizeAvailable -> TODO()

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