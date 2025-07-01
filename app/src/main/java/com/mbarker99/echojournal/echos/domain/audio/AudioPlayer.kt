package com.mbarker99.echojournal.echos.domain.audio

import androidx.compose.ui.graphics.Path
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayer {
    val activeTrack: StateFlow<AudioTrack?>
    fun play(filePath: String, onComplete: () -> Unit)
    fun pause()
    fun resume()
    fun stop()
}