package com.mbarker99.echojournal.echos.presentation.create_echo

import com.mbarker99.echojournal.echos.presentation.echos.model.TrackSizeInfo
import com.mbarker99.echojournal.echos.presentation.model.MoodUi

sealed interface CreateEchoAction {
    data object OnNavigateBackClick : CreateEchoAction
    data class OnTitleTextChanged(val text: String): CreateEchoAction
    data class OnAddTopicTextChanged(val text: String): CreateEchoAction
    data class OnNoteTextChanged(val text: String): CreateEchoAction
    data object OnSelectMoodClick: CreateEchoAction
    data object OnDismissMoodSelector: CreateEchoAction
    data class OnMoodClick(val mood: MoodUi): CreateEchoAction
    data object OnConfirmMood: CreateEchoAction
    data class OnTopicClick(val topic: String): CreateEchoAction
    data object OnDismissTopicSuggestions: CreateEchoAction
    data object OnCancelClick: CreateEchoAction
    data object OnSaveClick: CreateEchoAction
    data object OnPlayAudioClick: CreateEchoAction
    data object OnPauseAudioClick: CreateEchoAction
    data class OnTrackSizeAvailable(val trackSizeInfo: TrackSizeInfo): CreateEchoAction
    data class OnRemoveTopicClick(val topic: String): CreateEchoAction
    data object OnNavigateBack: CreateEchoAction
    data object OnDismissConfirmLeaveDialog: CreateEchoAction
}