package com.mbarker99.echojournal.echos.presentation.create_echo

import com.mbarker99.echojournal.core.presentation.designsystem.dropdowns.Selectable
import com.mbarker99.echojournal.echos.presentation.echos.model.PlaybackState
import com.mbarker99.echojournal.echos.presentation.model.MoodUi
import kotlin.time.Duration

data class CreateEchoState(
    val titleText: String = "",
    val addTopicText: String = "",
    val topics: List<String> = emptyList(),
    val noteText: String = "",
    val showMoodSelector: Boolean = true,
    val selectedMood: MoodUi = MoodUi.NEUTRAL,
    val showTopicSuggestions: Boolean = false,
    val mood: MoodUi? = null,
    val searchResults: List<Selectable<String>> = emptyList(),
    val showCreateTopicOption: Boolean = true,
    val canSaveEcho: Boolean = false,
    val playbackAmplitudes: List<Float> = emptyList(),
    val playbackTotalDuration: Duration = Duration.ZERO,
    val playbackState: PlaybackState = PlaybackState.STOPPED,
    val durationPlayed: Duration = Duration.ZERO,
    val showConfirmLeaveDialog: Boolean = false
) {
    val durationPlayedRatio = (durationPlayed / playbackTotalDuration).toFloat()
}
