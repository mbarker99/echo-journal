package com.plcoding.echojournal.echos.presentation.echos

import com.plcoding.echojournal.echos.presentation.echos.model.EchoFilterChip

sealed interface EchosAction {
    data object OnMoodChipClick: EchosAction
    data object OnTopicChipClicked: EchosAction
    data object OnFabClick: EchosAction
    data object OnFabLongPress: EchosAction
    data object OnSettingsClick: EchosAction
    data class OnRemoveFilters(val filterChip: EchoFilterChip): EchosAction
}