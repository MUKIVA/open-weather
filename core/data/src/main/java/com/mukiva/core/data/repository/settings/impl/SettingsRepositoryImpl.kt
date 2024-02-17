package com.mukiva.core.data.repository.settings.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mukiva.core.data.repository.settings.ISettingsRepository
import com.mukiva.core.data.repository.settings.entity.IntAppConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ISettingsRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)

    override suspend fun setTheme(theme: Int) {
        context.dataStore.edit { settings ->
            settings[DARK_THEME_MODE_KEY] = theme
        }
    }

    override suspend fun setUnitsType(type: Int) {
        context.dataStore.edit { settings ->
            settings[UNITS_TYPE_KEY] = type
        }
    }

    override suspend fun setLastForecastUpdateTime(date: String) {
        context.dataStore.edit { settings ->
            settings[LAST_FORECAST_UPDATE_TIME] = date
        }
    }

    override suspend fun getLastForecastUpdateTime(): String? {
        return context.dataStore.data.map { settings ->
            val date = settings[LAST_FORECAST_UPDATE_TIME]
            date
        }.last()
    }

    override fun asAppConfig(): Flow<IntAppConfig> {
        return context.dataStore.data
            .map { settings ->
                val theme = settings[DARK_THEME_MODE_KEY] ?: 0
                val unitsType = settings[UNITS_TYPE_KEY] ?: 0
                IntAppConfig(
                    theme = theme,         // Default SYSTEM
                    unitsType = unitsType  // Default METRIC
                )
            }
    }

    companion object {

        private const val DATA_STORE_NAME = "SETTINGS"

        val DARK_THEME_MODE_KEY = intPreferencesKey("DARK_THEME_MODE")
        val UNITS_TYPE_KEY = intPreferencesKey("UNITS_TYPE_KEY")
        val LAST_FORECAST_UPDATE_TIME = stringPreferencesKey("LAST_FORECAST_UPDATE_TIME")
    }
}