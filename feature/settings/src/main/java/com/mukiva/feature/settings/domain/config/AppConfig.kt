package com.mukiva.feature.settings.domain.config

import com.github.mukiva.open_weather.core.domain.UnitsType

data class AppConfig(
    val theme: Theme,
    val unitsType: UnitsType
)