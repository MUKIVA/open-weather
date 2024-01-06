package com.mukiva.impl.domain.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mukiva.api.SpeedUnitsType
import com.mukiva.api.Theme
import com.mukiva.api.TempUnitsType
import com.mukiva.api.domain.AppConfig
import com.mukiva.api.repository.IGeneralSettingsSetter
import com.mukiva.api.repository.ISettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : ISettingsRepository, IGeneralSettingsSetter {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)

    override suspend fun setTheme(theme: Theme) {
        context.dataStore.edit { settings ->
            settings[DARK_THEME_MODE_KEY] = theme.name
        }
    }

    override suspend fun setTempUnitsType(type: TempUnitsType) {
        context.dataStore.edit { settings ->
            settings[TEMP_UNITS_TYPE_KEY] = type.name
        }
    }

    override suspend fun setSpeedUnitsType(type: SpeedUnitsType) {
        context.dataStore.edit { settings ->
            settings[SPEED_UNITS_TYPE_KEY] = type.name
        }
    }

    override fun asAppConfig(): Flow<AppConfig> {
        return context.dataStore.data
            .map { settings ->
                val theme = settings[DARK_THEME_MODE_KEY]
                val tempUnitsType = settings[TEMP_UNITS_TYPE_KEY]
                val speedUnitsType = settings[SPEED_UNITS_TYPE_KEY]
                AppConfig(
                    theme = theme?.let { Theme.valueOf(theme) } ?: Theme.SYSTEM,
                    tempUnits = tempUnitsType?.let { TempUnitsType.valueOf(tempUnitsType) } ?: TempUnitsType.CELSIUS,
                    speedUnits = speedUnitsType?.let { SpeedUnitsType.valueOf(speedUnitsType) } ?: SpeedUnitsType.METRIC
                )
            }
    }

    companion object {

        private const val DATA_STORE_NAME = "SETTINGS"

        val DARK_THEME_MODE_KEY = stringPreferencesKey("DARK_THEME_MODE")
        val TEMP_UNITS_TYPE_KEY = stringPreferencesKey("UNITS_TYPE")
        val SPEED_UNITS_TYPE_KEY = stringPreferencesKey("SPEED_UNITS_TYPE_KEY")

    }

}