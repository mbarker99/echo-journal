package com.mbarker99.echojournal.core.db.echo

import androidx.room.TypeConverter
import com.mbarker99.echojournal.echos.presentation.model.MoodUi

class MoodUiTypeConverter {
    @TypeConverter
    fun fromMood(mood: MoodUi): String {
        return mood.name
    }
    @TypeConverter
    fun toMood(moodName: String): MoodUi {
        return MoodUi.valueOf(moodName)
    }
}