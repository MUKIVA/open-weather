package com.github.mukiva.feature.dashboard.domain.model

import com.github.mukiva.openweather.core.domain.weather.Precipitation

data class Precipitation(
    val willItRain: Boolean,
    val chanceOfRain: Int,
    val willItSnow: Boolean,
    val chanceOfSnow: Int,
    val precipitationAmount: Precipitation
)