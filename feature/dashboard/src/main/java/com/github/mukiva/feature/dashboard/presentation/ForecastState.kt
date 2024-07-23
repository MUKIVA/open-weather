package com.github.mukiva.feature.dashboard.presentation

import com.github.mukiva.feature.dashboard.domain.model.DayForecast

data class ForecastState(
    val days: List<DayForecast>
)