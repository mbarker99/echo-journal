package com.mbarker99.echojournal.echos.presentation.echos

import com.mbarker99.echojournal.echos.presentation.echos.model.EchoFilterChip
import com.mbarker99.echojournal.echos.presentation.echos.model.TrackSizeInfo
import com.mbarker99.echojournal.echos.presentation.model.MoodUi

sealed interface EchosAction {
    data object OnMoodChipClick: EchosAction
    data object OnDismissMoodDropDown: EchosAction
    data class OnFilterByMoodClick(val moodUi: MoodUi): EchosAction
    data object OnTopicChipClicked: EchosAction
    data object OnDismissTopicDropDown: EchosAction
    data class OnFilterByTopicClick(val topic: String): EchosAction
    data object OnRecordFabClick: EchosAction
    data object OnRequestPermissionQuickRecording: EchosAction
    data object OnRecordButtonLongClick: EchosAction
    data object OnSettingsClick: EchosAction
    data class OnRemoveFilters(val filterType: EchoFilterChip): EchosAction
    data class OnPlayEchoClick(val echoId: Int): EchosAction
    data object OnPauseEchoClick : EchosAction

    data class OnTrackSizeAvailable(val trackSize: TrackSizeInfo) : EchosAction
    data object OnAudioPermissionGranted: EchosAction

    // Recording
    data object OnPauseRecordingClick : EchosAction
    data object OnResumeRecordingClick : EchosAction
    data object OnCompleteRecordingClick : EchosAction
    data object OnCancelRecordingClick : EchosAction

}