package com.mukiva.api.repository

import com.mukiva.api.Theme
import com.mukiva.api.UnitsType

interface IGeneralSettingsSetter {
    suspend fun setTheme(theme: Theme)
    suspend fun setUnitsType(type: UnitsType)
}