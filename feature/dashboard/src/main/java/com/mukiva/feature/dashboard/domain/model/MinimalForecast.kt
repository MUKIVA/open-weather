package com.mukiva.feature.dashboard.domain.model

import com.github.mukiva.open_weather.core.domain.Temp
import kotlinx.datetime.LocalDateTime

data class MinimalForecast(
    val id: Int,
    val avgTemp: Temp,
    val minTemp: Temp,
    val maxTemp: Temp,
    val conditionIconUrl: String,
    val date: LocalDateTime,
)