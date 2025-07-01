package com.mbarker99.echojournal.echos.domain.echo.model

import java.time.Instant
import kotlin.time.Duration

data class Echo(
    val mood: Mood,
    val title: String,
    val note: String?,
    val topics: List<String>,
    val audioFilePath: String,
    val audioPlaybackLength: Duration,
    val audioAmplitudes: List<Float>,
    val recordedAt: Instant,
    val id: Int? = null
)