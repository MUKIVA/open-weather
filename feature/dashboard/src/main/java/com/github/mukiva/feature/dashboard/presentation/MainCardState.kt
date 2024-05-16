package com.github.mukiva.feature.dashboard.presentation

import com.github.mukiva.openweather.core.domain.weather.Temp

sealed class MainCardState {
    data object Loading : MainCardState()
    data class Content(
        val locationName: String,
        val currentTemp: Temp,
    ) : MainCardState()
}