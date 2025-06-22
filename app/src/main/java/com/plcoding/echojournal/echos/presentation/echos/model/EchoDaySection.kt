package com.plcoding.echojournal.echos.presentation.echos.model

import com.plcoding.echojournal.core.presentation.util.UiText
import com.plcoding.echojournal.echos.presentation.model.EchoUi

data class EchoDaySection(
    val dateHeader: UiText,
    val echos: List<EchoUi>
)
