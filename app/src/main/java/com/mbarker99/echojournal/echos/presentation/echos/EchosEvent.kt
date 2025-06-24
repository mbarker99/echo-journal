package com.mbarker99.echojournal.echos.presentation.echos

import com.mbarker99.echojournal.echos.domain.recording.RecordingDetails

sealed interface EchosEvent {
    data object RequestAudioPermission: EchosEvent
    data object RecordingTooShort: EchosEvent
    data class OnCompleteRecording(val recordingDetails: RecordingDetails): EchosEvent
}