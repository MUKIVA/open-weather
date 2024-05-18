package com.github.mukiva.feature.dashboard.domain.model

import com.github.mukiva.openweather.core.domain.weather.Temp
import kotlinx.datetime.LocalDateTime

data class MinimalForecast(
    val id: Int,
    val avgTemp: Temp,
    val minTemp: Temp,
    val maxTemp: Temp,
    val conditionIconCode: Int,
    val isDay: Boolean,
    val date: LocalDateTime,
)
