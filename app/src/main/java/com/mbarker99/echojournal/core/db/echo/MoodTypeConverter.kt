package com.mbarker99.echojournal.core.db.echo

import androidx.room.TypeConverter
import com.mbarker99.echojournal.echos.domain.echo.model.Mood

class MoodTypeConverter {
    @TypeConverter
    fun fromMood(mood: Mood): String {
        return mood.name
    }
    @TypeConverter
    fun toMood(moodName: String): Mood {
        return Mood.valueOf(moodName)
    }
}