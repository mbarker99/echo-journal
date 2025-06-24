package com.mbarker99.echojournal.echos.presentation.echos.model

import com.mbarker99.echojournal.core.presentation.util.UiText
import com.mbarker99.echojournal.echos.presentation.model.EchoUi

data class EchoDaySection(
    val dateHeader: UiText,
    val echos: List<EchoUi>
)
