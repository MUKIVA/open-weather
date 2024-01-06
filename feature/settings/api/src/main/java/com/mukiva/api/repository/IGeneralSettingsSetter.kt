package com.mukiva.api.repository

import com.mukiva.api.SpeedUnitsType
import com.mukiva.api.Theme
import com.mukiva.api.TempUnitsType

interface IGeneralSettingsSetter {
    suspend fun setTheme(theme: Theme)
    suspend fun setTempUnitsType(type: TempUnitsType)
    suspend fun setSpeedUnitsType(type: SpeedUnitsType)
}