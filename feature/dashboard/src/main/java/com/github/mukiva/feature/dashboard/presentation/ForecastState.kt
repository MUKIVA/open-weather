package com.github.mukiva.feature.dashboard.presentation

import com.github.mukiva.feature.dashboard.domain.model.DayForecast

internal data class ForecastState(
    val days: List<DayForecast>
)