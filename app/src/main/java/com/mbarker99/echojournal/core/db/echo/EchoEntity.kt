package com.mbarker99.echojournal.core.db.echo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mbarker99.echojournal.echos.presentation.model.MoodUi

@Entity
data class EchoEntity(
    @PrimaryKey(autoGenerate = true)
    val echoId: Int = 0,
    val title: String,
    val mood: MoodUi,
    val recordedAt: Long,
    val note: String?,
    val audioFilePath: String,
    val audioPlaybackLength: Long,
    val audioAmplitudes: List<Float>
)
