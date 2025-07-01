package com.mbarker99.echojournal.echos.data.echo

import com.mbarker99.echojournal.core.db.echo.EchoEntity
import com.mbarker99.echojournal.core.db.echo_topic_relation.EchoWithTopics
import com.mbarker99.echojournal.core.db.topic.TopicEntity
import com.mbarker99.echojournal.echos.domain.echo.model.Echo
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

fun EchoWithTopics.toEcho() : Echo {
    return Echo(
        mood = echo.mood,
        title = echo.title,
        note = echo.note,
        topics = topics.map { it.topic },
        audioFilePath = echo.audioFilePath,
        audioPlaybackLength = echo.audioPlaybackLength.milliseconds,
        audioAmplitudes = echo.audioAmplitudes,
        recordedAt = Instant.ofEpochMilli(this.echo.recordedAt),
        id = echo.echoId
    )
}

fun Echo.toEchoWithTopics(): EchoWithTopics {
    return EchoWithTopics(
        echo = EchoEntity(
            echoId = id ?: 0,
            title = title,
            mood = mood,
            recordedAt = recordedAt.toEpochMilli(),
            note = note,
            audioFilePath = audioFilePath,
            audioPlaybackLength = audioPlaybackLength.inWholeMilliseconds,
            audioAmplitudes = audioAmplitudes
        ),
        topics = topics.map { TopicEntity(it) }
    )
}