package com.plcoding.echojournal.echos.presentation.echos

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class EchosViewModel : ViewModel() {

    private val _state = MutableStateFlow(EchosState())
    val state = _state

    fun onAction(action: EchosAction) {
        when (action) {
            EchosAction.OnFabClick -> {

            }

            EchosAction.OnFabLongPress -> {

            }

            EchosAction.OnMoodChipClick -> {

            }

            is EchosAction.OnRemoveFilters -> {

            }

            EchosAction.OnTopicChipClicked -> {

            }

            EchosAction.OnSettingsClick -> {

            }
        }
    }
}