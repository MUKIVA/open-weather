package com.github.mukiva.weather_data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.mukiva.open_weather.core.domain.settings.Config
import com.github.mukiva.open_weather.core.domain.settings.Group
import com.github.mukiva.open_weather.core.domain.settings.Theme
import com.github.mukiva.open_weather.core.domain.settings.UnitsType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)

    suspend fun setTheme(theme: Theme) {
        context.dataStore.edit { settings ->
            settings[THEME_MODE_KEY] = theme.ordinal
        }
    }

    fun getTheme(): Flow<Theme> {
        return context.dataStore.data.map { settings ->
            Theme.entries[(settings[THEME_MODE_KEY] ?: 0)]
        }
    }

    suspend fun setUnitsType(unitsType: UnitsType) {
        context.dataStore.edit { settings ->
            settings[UNITS_TYPE_KEY] = unitsType.ordinal
        }
    }

    fun getUnitsType(): Flow<UnitsType> {
        return context.dataStore.data.map { settings ->
            UnitsType.entries[(settings[UNITS_TYPE_KEY] ?: 0)]
        }
    }

    fun getConfiguration(): Flow<Config> {
        val general = getGeneral()
        return general.map { group ->
            Config(groups = listOf(group))
        }
    }

    private fun getGeneral(): Flow<Group.General> {
        val themeFlow = getTheme()
        val unitsTypeFlow = getUnitsType()
        return themeFlow.combine(unitsTypeFlow) { theme, unitsType ->
            Group.General(
                theme = theme,
                unitsType = unitsType,
            )
        }
    }

    companion object {
        private const val DATA_STORE_NAME = "SETTINGS"

        private val THEME_MODE_KEY = intPreferencesKey("THEME_MODE_KEY")
        private val UNITS_TYPE_KEY = intPreferencesKey("UNITS_TYPE_KEY")

    }
}