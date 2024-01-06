package com.mukiva.api.domain

import com.mukiva.api.SpeedUnitsType
import com.mukiva.api.Theme
import com.mukiva.api.TempUnitsType

data class AppConfig(
    val theme: Theme,
    val tempUnits: TempUnitsType,
    val speedUnits: SpeedUnitsType
)