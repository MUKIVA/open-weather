package com.mukiva.api.domain

import com.mukiva.api.UnitsType
import com.mukiva.api.Theme

data class AppConfig(
    val theme: Theme,
    val unitsType: UnitsType
)