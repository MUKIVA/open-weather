package com.mukiva.feature.dashboard.presentation

import com.mukiva.feature.dashboard.domain.model.CurrentWeather
import com.mukiva.feature.dashboard.domain.model.Location
import com.mukiva.feature.dashboard.domain.model.MinimalForecast

data class MinorWeatherState(
    val type: Type,
    val location: Location,
    val currentWeather: CurrentWeather?,
    val minimalForecastState: Collection<MinimalForecast>
) {
    enum class Type {
        LOADING,
        CONTENT
    }
}