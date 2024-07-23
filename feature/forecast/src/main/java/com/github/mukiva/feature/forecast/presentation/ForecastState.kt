package com.github.mukiva.feature.forecast.presentation

internal sealed class ForecastState {
    data object Init : ForecastState()
    data object Error : ForecastState()
    data object Loading : ForecastState()
    data class Content(
        val hourlyForecast: List<HourlyForecast>,
    ) : ForecastState()
}
