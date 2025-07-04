package com.mbarker99.echojournal.app.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Echos: Route

    @Serializable
    data class CreateEcho(
        val recordingPath: String,
        val duration: Long,
        val amplitudes: String
    ): Route

    @Serializable
    data object Settings: Route
}