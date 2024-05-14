package com.github.mukiva.feature.dashboard.presentation

import com.github.mukiva.feature.dashboard.domain.model.Forecast

sealed class LocationWeatherState {
    data object Init : LocationWeatherState()
    data object Loading : LocationWeatherState()
    data object Error : LocationWeatherState()
    data class Content(
        val forecast: Forecast,
    ) : LocationWeatherState()
}
