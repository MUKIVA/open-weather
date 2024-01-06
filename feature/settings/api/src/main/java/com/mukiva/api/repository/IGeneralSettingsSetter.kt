package com.mukiva.api.repository

import com.mukiva.api.UnitsType
import com.mukiva.api.Theme

interface IGeneralSettingsSetter {
    suspend fun setTheme(theme: Theme)
    suspend fun setUnitsType(type: UnitsType)
}