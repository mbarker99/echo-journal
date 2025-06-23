package com.mbarker99.echojournal.echos.presentation.echos.model

import com.mbarker99.echojournal.R
import com.mbarker99.echojournal.core.presentation.util.UiText

data class MoodChipContent (
    val iconsRes: List<Int> = emptyList(),
    val title: UiText = UiText.StringResource(R.string.all_moods)
)