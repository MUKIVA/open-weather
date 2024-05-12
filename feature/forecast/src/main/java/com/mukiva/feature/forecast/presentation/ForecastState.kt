package com.mukiva.feature.forecast.presentation

sealed class ForecastState {
    data object Init : ForecastState()
    data object Error : ForecastState()
    data object Loading : ForecastState()
    data class Content(
        val hourlyForecast: List<HourlyForecast.Content>,
    ) : ForecastState()
}