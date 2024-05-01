package com.mukiva.feature.forecast.presentation

import com.mukiva.feature.forecast.domain.UnitsType

sealed class ForecastState {
    data object Init : ForecastState()
    data object Error : ForecastState()
    data object Loading : ForecastState()
    data class Content(
        val unitsType: UnitsType,
        val hourlyForecast: List<HourlyForecast>,
    ) : ForecastState()
}