package com.mbarker99.echojournal.echos.presentation.create_echo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbarker99.echojournal.echos.presentation.echos.EchosAction
import com.mbarker99.echojournal.echos.presentation.echos.EchosState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

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
            CreateEchoAction.OnDismissMoodSelector -> TODO()
            is CreateEchoAction.OnAddTopicTextChanged -> TODO()
            CreateEchoAction.OnCancelClick -> TODO()
            CreateEchoAction.OnConfirmMood -> TODO()
            CreateEchoAction.OnCreateNewTopicClick -> TODO()
            CreateEchoAction.OnDismissTopicSuggestions -> TODO()
            is CreateEchoAction.OnMoodClick -> TODO()
            CreateEchoAction.OnNavigateBackClick -> TODO()
            is CreateEchoAction.OnNoteTextChanged -> TODO()
            CreateEchoAction.OnPauseAudioClick -> TODO()
            CreateEchoAction.OnPlayAudioClick -> TODO()
            is CreateEchoAction.OnRemoveTopicClick -> TODO()
            CreateEchoAction.OnSaveClick -> TODO()
            is CreateEchoAction.OnTitleTextChanged -> TODO()
            is CreateEchoAction.OnTopicClick -> TODO()
            is CreateEchoAction.OnTrackSizeAvailable -> TODO()
            CreateEchoAction.OnSelectMoodClick -> TODO()
        }
    }
}