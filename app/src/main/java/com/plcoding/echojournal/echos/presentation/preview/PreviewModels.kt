package com.plcoding.echojournal.echos.presentation.preview

import com.plcoding.echojournal.echos.presentation.echos.model.PlaybackState
import com.plcoding.echojournal.echos.presentation.model.EchoUi
import com.plcoding.echojournal.echos.presentation.model.MoodUi
import java.time.Instant
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

data object PreviewModels {
    val echoUi = EchoUi(
        id = 0,
        title = "My audio memo",
        mood = MoodUi.STRESSED,
        recordedAt = Instant.now(),
        note = buildString { repeat(50) { append("Hello ") } },
        topics = listOf("Love"),
        amplitudes = (1..30).map { Random.nextFloat() },
        playbackCurrentDuration = 125.seconds,
        playbackTotalDuration = 250.seconds,
        playbackState = PlaybackState.PAUSED
    )
}
