package com.mbarker99.echojournal.echos.presentation.echos.util

import com.mbarker99.echojournal.echos.domain.echo.model.Echo
import com.mbarker99.echojournal.echos.presentation.echos.model.PlaybackState
import com.mbarker99.echojournal.echos.presentation.model.EchoUi
import com.mbarker99.echojournal.echos.presentation.model.MoodUi
import kotlin.time.Duration

fun Echo.toEchoUi(
    currentPlaybackDuration: Duration = Duration.ZERO,
    playbackState: PlaybackState = PlaybackState.STOPPED
): EchoUi {
    return EchoUi(
        id = id!!,
        title = title,
        mood = MoodUi.valueOf(mood.name),
        recordedAt = recordedAt,
        note = note,
        topics = topics,
        amplitudes = audioAmplitudes,
        playbackTotalDuration = audioPlaybackLength,
        audioFilePath = audioFilePath,
        playbackCurrentDuration = currentPlaybackDuration,
        playbackState = playbackState
    )
}