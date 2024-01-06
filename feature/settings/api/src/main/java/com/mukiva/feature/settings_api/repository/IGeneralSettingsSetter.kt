package com.mukiva.feature.settings_api.repository

import com.mukiva.feature.settings_api.UnitsType
import com.mukiva.feature.settings_api.Theme

interface IGeneralSettingsSetter {
    suspend fun setTheme(theme: Theme)
    suspend fun setUnitsType(type: UnitsType)
}