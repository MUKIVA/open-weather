package com.github.mukiva.feature.forecast.presentation

import com.github.mukiva.feature.forecast.domain.ForecastItem
import kotlinx.datetime.LocalDateTime

data class HourlyForecast(
    val index: Int,
    val date: LocalDateTime,
    val hours: List<ForecastItem>,
)
