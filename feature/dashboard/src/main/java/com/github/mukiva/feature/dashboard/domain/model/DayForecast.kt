package com.github.mukiva.feature.dashboard.domain.model

import com.github.mukiva.openweather.core.domain.weather.Temp
import kotlinx.datetime.LocalDateTime

internal data class DayForecast(
    val id: Int,
    val date: LocalDateTime,
    val nightTemp: Temp,
    val dayTemp: Temp,
    val conditionIconCode: Int
)