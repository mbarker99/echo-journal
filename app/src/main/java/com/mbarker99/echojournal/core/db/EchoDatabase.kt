package com.mbarker99.echojournal.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mbarker99.echojournal.core.db.echo.EchoDao
import com.mbarker99.echojournal.core.db.echo.EchoEntity
import com.mbarker99.echojournal.core.db.echo.FloatListTypeConverter
import com.mbarker99.echojournal.core.db.echo.MoodTypeConverter
import com.mbarker99.echojournal.core.db.echo_topic_relation.EchoTopicCrossRef
import com.mbarker99.echojournal.core.db.topic.TopicEntity

@Database(
    version = 1,
    entities = [EchoEntity::class, TopicEntity::class, EchoTopicCrossRef::class]
)
@TypeConverters(
    MoodTypeConverter::class,
    FloatListTypeConverter::class
)
abstract class EchoDatabase: RoomDatabase() {
    abstract val echoDao: EchoDao
}