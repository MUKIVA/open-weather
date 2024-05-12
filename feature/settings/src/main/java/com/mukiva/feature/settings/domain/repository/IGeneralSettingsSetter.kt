package com.mukiva.feature.settings.domain.repository

import com.github.mukiva.open_weather.core.domain.UnitsType
import com.mukiva.feature.settings.domain.config.Theme

interface IGeneralSettingsSetter {
    suspend fun setTheme(theme: Theme)
    suspend fun setUnitsType(type: UnitsType)
}