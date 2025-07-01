package com.mbarker99.echojournal.echos.domain.echo

import com.mbarker99.echojournal.echos.domain.echo.model.Echo
import kotlinx.coroutines.flow.Flow

interface EchoDataSource {
    fun observeEchos(): Flow<List<Echo>>
    fun observeTopics(): Flow<List<String>>
    fun searchTopics(query: String): Flow<List<String>>
    suspend fun insertEcho(echo: Echo)
}