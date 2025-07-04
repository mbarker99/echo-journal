package com.mbarker99.echojournal.echos.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mbarker99.echojournal.echos.domain.echo.model.Mood
import com.mbarker99.echojournal.echos.domain.settings.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class SettingsPreferencesImpl(
    private val context: Context
): SettingsPreferences {

    companion object {
        private val Context.settingsDataStore by preferencesDataStore(
            name = "settings_datastore"
        )
    }

    private val topicsKey = stringSetPreferencesKey("topics")
    private val moodKey = stringPreferencesKey("mood")

    override suspend fun saveDefaultTopics(topics: List<String>) {
        context.settingsDataStore.edit { prefs ->
            prefs[topicsKey] = topics.toSet()
        }
    }

    override fun observeDefaultTopics(): Flow<List<String>> {
        return context
            .settingsDataStore
            .data
            .map { prefs ->
                prefs[topicsKey]?.toList() ?: emptyList()
            }
            .distinctUntilChanged()
    }

    override suspend fun saveDefaultMood(mood: Mood) {
        context.settingsDataStore.edit { prefs ->
            prefs[moodKey] = mood.name
        }
    }

    override fun observeDefaultMood(): Flow<Mood> {
        return context
            .settingsDataStore
            .data
            .map { prefs ->
                prefs[moodKey]?.let {
                    Mood.valueOf(it)
                } ?: Mood.NEUTRAL
            }
            .distinctUntilChanged()
    }
}