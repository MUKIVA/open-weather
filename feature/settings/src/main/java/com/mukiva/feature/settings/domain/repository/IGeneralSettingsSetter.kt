package com.mukiva.feature.settings.domain.repository

import com.mukiva.feature.settings.domain.config.Theme
import com.mukiva.feature.settings.domain.config.UnitsType

interface IGeneralSettingsSetter {
    suspend fun setTheme(theme: Theme)
    suspend fun setUnitsType(type: UnitsType)
}