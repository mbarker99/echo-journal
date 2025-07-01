package com.mbarker99.echojournal.echos.presentation.echos.util

import com.mbarker99.echojournal.app.navigation.Route
import com.mbarker99.echojournal.echos.domain.recording.RecordingDetails
import kotlin.time.Duration.Companion.milliseconds

fun RecordingDetails.toCreateEchoRoute(): Route.CreateEcho {
    return Route.CreateEcho(
        recordingPath = this.filePath ?: throw IllegalArgumentException(
            "Recording path can't be null"
        ),
        duration = this.duration.inWholeMilliseconds,
        amplitudes = this.amplitudes.joinToString(";")
    )
}

fun Route.CreateEcho.toRecordingDetails(): RecordingDetails {
    return RecordingDetails(
        duration = this.duration.milliseconds,
        amplitudes = this.amplitudes.split(";").map { it.toFloat() },
        filePath = recordingPath
    )
}