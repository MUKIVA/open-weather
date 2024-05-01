package com.mukiva.feature.forecast.presentation

import java.util.Date

data class HourlyForecast(
    val id: Int,
    val date: Date,
    val groups: List<ForecastGroup>,
)