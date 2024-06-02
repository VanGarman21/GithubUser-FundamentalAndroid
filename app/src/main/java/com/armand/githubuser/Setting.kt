package com.armand.githubuser

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Setting private constructor(private val dataStore: DataStore<Preferences>) {
    // DataStore<Preferences> used to store and retrieve preferences asynchronously

    private val theme = booleanPreferencesKey("theme_setting")
    // Key for the boolean preference representing the theme setting

    fun getThemeMode(): Flow<Boolean> {
        // Returns a Flow<Boolean> representing the current theme setting
        return dataStore.data.map { preferences ->
            // Maps the preferences to a boolean value, defaulting to false if not found
            preferences[theme] ?: false
        }
    }

    suspend fun saveThemeMode(isDarkModeActive: Boolean) {
        // Saves the theme setting asynchronously
        dataStore.edit { preferences ->
            // Modifies the preferences with the new theme setting
            preferences[theme] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: Setting? = null

        fun getInstance(dataStore: DataStore<Preferences>): Setting {
            // Ensures singleton pattern for Setting instance
            return INSTANCE ?: synchronized(this) {
                // Uses synchronized block to handle concurrent access
                INSTANCE ?: createInstance(dataStore).also { INSTANCE = it }
            }
        }

        private fun createInstance(dataStore: DataStore<Preferences>): Setting {
            // Creates a new instance of Setting with the provided DataStore
            return Setting(dataStore)
        }
    }

}
