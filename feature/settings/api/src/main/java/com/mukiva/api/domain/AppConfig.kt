package com.mukiva.api.domain

import com.mukiva.api.Theme
import com.mukiva.api.UnitsType

data class AppConfig(
    val theme: Theme,
    val units: UnitsType,
)