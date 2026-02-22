package com.wouterdevriendt.trivit.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class UserPreferencesData(
    val hapticsEnabled: Boolean = true,
    val hideCounterWhenExpanded: Boolean = false,
    val tutorialSeen: Boolean = false,
    val colorSchemeIndex: Int = 0,
    val nextColorIndex: Int = 0
)

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    private object Keys {
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val HIDE_COUNTER = booleanPreferencesKey("hide_counter_when_expanded")
        val TUTORIAL_SEEN = booleanPreferencesKey("tutorial_seen")
        val COLOR_SCHEME_INDEX = intPreferencesKey("color_scheme_index")
        val NEXT_COLOR_INDEX = intPreferencesKey("next_color_index")
    }

    val preferences: Flow<UserPreferencesData> = dataStore.data.map { prefs ->
        UserPreferencesData(
            hapticsEnabled = prefs[Keys.HAPTICS_ENABLED] ?: true,
            hideCounterWhenExpanded = prefs[Keys.HIDE_COUNTER] ?: false,
            tutorialSeen = prefs[Keys.TUTORIAL_SEEN] ?: false,
            colorSchemeIndex = prefs[Keys.COLOR_SCHEME_INDEX] ?: 0,
            nextColorIndex = prefs[Keys.NEXT_COLOR_INDEX] ?: 0
        )
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.HAPTICS_ENABLED] = enabled }
    }

    suspend fun setHideCounter(hide: Boolean) {
        dataStore.edit { it[Keys.HIDE_COUNTER] = hide }
    }

    suspend fun setTutorialSeen(seen: Boolean) {
        dataStore.edit { it[Keys.TUTORIAL_SEEN] = seen }
    }

    suspend fun setColorSchemeIndex(index: Int) {
        dataStore.edit { it[Keys.COLOR_SCHEME_INDEX] = index }
    }

    suspend fun setNextColorIndex(index: Int) {
        dataStore.edit { it[Keys.NEXT_COLOR_INDEX] = index }
    }

    suspend fun getAndIncrementColorIndex(paletteSize: Int): Int {
        var currentIndex = 0
        dataStore.edit { prefs ->
            currentIndex = prefs[Keys.NEXT_COLOR_INDEX] ?: 0
            prefs[Keys.NEXT_COLOR_INDEX] = (currentIndex + 1) % paletteSize
        }
        return currentIndex
    }
}
