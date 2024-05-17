package com.github.mukiva.feature.dashboard.presentation

import kotlinx.coroutines.flow.StateFlow

interface IForecastStateHolder {
    val forecastState: StateFlow<LocationWeatherState>
}
