package com.mukiva.feature.forecast.presentation

import kotlinx.datetime.LocalDateTime

data class HourlyForecast(
    val index: Int,
    val date: LocalDateTime,
    val groups: List<ForecastGroup>,
)