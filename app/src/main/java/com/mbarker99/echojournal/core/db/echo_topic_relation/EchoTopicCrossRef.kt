package com.mbarker99.echojournal.core.db.echo_topic_relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.mbarker99.echojournal.core.db.echo.EchoEntity
import com.mbarker99.echojournal.core.db.topic.TopicEntity

@Entity(
    primaryKeys = ["echoId", "topic"]
)
data class EchoTopicCrossRef(
    val echoId: Int,
    val topic: String,
)

data class EchoWithTopics(
    @Embedded val echo: EchoEntity,
    @Relation(
        parentColumn = "echoId",
        entityColumn = "topic",
        associateBy = Junction(EchoTopicCrossRef::class)
    )
    val topics: List<TopicEntity>
)
