package com.mbarker99.echojournal.echos.presentation.echos

sealed interface EchosEvent {
    data object RequestAudioPermission: EchosEvent
}