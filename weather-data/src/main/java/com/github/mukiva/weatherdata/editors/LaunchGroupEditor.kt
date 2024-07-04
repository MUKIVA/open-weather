package com.github.mukiva.weatherdata.editors

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class LaunchGroupEditor(
    private val dataStore: DataStore<Preferences>
) : ILaunchGroupEditor {

    override suspend fun setFirstOpenComplete(isComplete: Boolean) {
        dataStore.edit { settings ->
            settings[FIRST_OPEN_COMPLETE_KEY] = isComplete
        }
    }

    override fun getFirstOpenComplete(): Flow<Boolean> {
        return dataStore.data.map { settings ->
            settings[FIRST_OPEN_COMPLETE_KEY] ?: false
        }
    }

    companion object {
        private val FIRST_OPEN_COMPLETE_KEY = booleanPreferencesKey("FIRST_OPEN_COMPLETE_KEY")
    }
}