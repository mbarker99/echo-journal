package com.mbarker99.echojournal.echos.presentation.echos

import com.mbarker99.echojournal.R
import com.mbarker99.echojournal.core.presentation.designsystem.dropdowns.Selectable
import com.mbarker99.echojournal.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import com.mbarker99.echojournal.core.presentation.util.UiText
import com.mbarker99.echojournal.echos.presentation.echos.model.EchoDaySection
import com.mbarker99.echojournal.echos.presentation.echos.model.EchoFilterChip
import com.mbarker99.echojournal.echos.presentation.echos.model.MoodChipContent
import com.mbarker99.echojournal.echos.presentation.model.EchoUi
import com.mbarker99.echojournal.echos.presentation.model.MoodUi

data class EchosState(
    val echos: Map<UiText, List<EchoUi>> = emptyMap(),
    val hasEchosRecorded: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveMoodFilters: Boolean = false,
    val isLoadingData: Boolean = false,
    val moods: List<Selectable<MoodUi>> = emptyList(),
    val topics: List<Selectable<String>> = listOf("Love", "Happy", "Work").asUnselectedItems(),
    val moodChipContent: MoodChipContent = MoodChipContent(),
    val selectedEchoFilterChip: EchoFilterChip? = null,
    val topicChipTitle: UiText = UiText.StringResource(R.string.all_topics)
) {
    val echoDaySections = echos
        .toList()
        .map { (dateHeader, echos) ->
            EchoDaySection(dateHeader, echos)
        }
}
