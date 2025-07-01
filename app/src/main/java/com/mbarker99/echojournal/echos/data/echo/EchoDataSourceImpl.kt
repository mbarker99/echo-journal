package com.mbarker99.echojournal.echos.data.echo

import com.mbarker99.echojournal.core.db.echo.EchoDao
import com.mbarker99.echojournal.echos.domain.echo.EchoDataSource
import com.mbarker99.echojournal.echos.domain.echo.model.Echo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class EchoDataSourceImpl(
    private val echoDao: EchoDao
) : EchoDataSource {
    override fun observeEchos(): Flow<List<Echo>> = echoDao.observeEchos()
        .map { echoWithTopics ->
            echoWithTopics.map { echoWithTopics ->
                echoWithTopics.toEcho()
            }
        }

    override fun observeTopics(): Flow<List<String>> = echoDao.observeTopics()
        .map { topicEntities ->
            topicEntities.map { it.topic }
        }

    override fun searchTopics(query: String): Flow<List<String>> = echoDao.searchTopics(query)
        .map { topicEntities ->
            topicEntities.map { it.topic }
        }

    override suspend fun insertEcho(echo: Echo) =
        echoDao.insertEchoWithTopics(echo.toEchoWithTopics())

}