package com.mukiva.feature.dashboard.presentation

import com.mukiva.feature.dashboard.domain.model.Forecast
import com.mukiva.feature.dashboard.domain.model.UnitsType

sealed class LocationWeatherState {
    data object Init : LocationWeatherState()
    data object Loading : LocationWeatherState()
    data object Error : LocationWeatherState()
    data class Content(
        val unitsType: UnitsType,
        val forecast: Forecast,
    ) : LocationWeatherState()
}