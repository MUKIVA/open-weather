package com.github.mukiva.weatherdata.editors

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.github.mukiva.openweather.core.domain.settings.Group
import com.github.mukiva.openweather.core.domain.settings.Lang
import com.github.mukiva.openweather.core.domain.settings.Theme
import com.github.mukiva.openweather.core.domain.settings.UnitsType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

internal class GeneralGroupEditor(
    private val dataStore: DataStore<Preferences>
) : IGeneralGroupEditor {

    override suspend fun setTheme(theme: Theme) {
        dataStore.edit { settings ->
            settings[THEME_MODE_KEY] = theme.ordinal
        }
    }

    override fun getTheme(): Flow<Theme> {
        return dataStore.data.map { settings ->
            Theme.entries[(settings[THEME_MODE_KEY] ?: 0)]
        }
    }

    override suspend fun setUnitsType(unitsType: UnitsType) {
        dataStore.edit { settings ->
            settings[UNITS_TYPE_KEY] = unitsType.ordinal
        }
    }

    override fun getUnitsType(): Flow<UnitsType> {
        return dataStore.data.map { settings ->
            UnitsType.entries[(settings[UNITS_TYPE_KEY] ?: 0)]
        }
    }

    override suspend fun setLocalization(lang: Lang) {
        dataStore.edit { settings ->
            settings[LOCALIZATION_KEY] = lang.ordinal
        }
    }

    override fun getLocalization(): Flow<Lang> {
        return dataStore.data.map { settings ->
            Lang.entries[(settings[LOCALIZATION_KEY] ?: 0)]
        }
    }

    override fun getGeneralGroup(): Flow<Group.General> {
        val themeFlow = getTheme()
        val unitsTypeFlow = getUnitsType()
        val localizationFlow = getLocalization()
        return combine(
            themeFlow,
            unitsTypeFlow,
            localizationFlow
        ) { theme, unitsType, localization ->
            Group.General(
                theme = theme,
                unitsType = unitsType,
                lang = localization
            )
        }
    }

    companion object {
        private val THEME_MODE_KEY = intPreferencesKey("THEME_MODE_KEY")
        private val UNITS_TYPE_KEY = intPreferencesKey("UNITS_TYPE_KEY")
        private val LOCALIZATION_KEY = intPreferencesKey("LOCALIZATION_KEY")
    }
}