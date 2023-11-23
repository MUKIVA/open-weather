package com.mukiva.api

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mukiva.api.navigation.AppConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConfigStore(
    private val context: Application
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)

    suspend fun setTheme(theme: Theme) {
        context.dataStore.edit { settings ->
            settings[DARK_THEME_MODE_KEY] = theme.name
        }
    }

    suspend fun setUnitsType(type: UnitsType) {
        context.dataStore.edit { settings ->
            settings[UNITS_TYPE_KEY] = type.name
        }
    }

    fun asAppConfig(): Flow<AppConfig> {
        return context.dataStore.data
            .map { settings ->
                val theme = settings[DARK_THEME_MODE_KEY]
                val unitsType = settings[UNITS_TYPE_KEY]
                AppConfig(
                    theme = theme?.let { Theme.valueOf(theme) } ?: Theme.SYSTEM ,
                    units = unitsType?.let { UnitsType.valueOf(unitsType) } ?: UnitsType.CELSIUS
                )
            }
    }

    companion object {

        private const val DATA_STORE_NAME = "SETTINGS"

        //Settings
        val DARK_THEME_MODE_KEY = stringPreferencesKey("DARK_THEME_MODE")
        val UNITS_TYPE_KEY = stringPreferencesKey("UNITS_TYPE")

    }

}