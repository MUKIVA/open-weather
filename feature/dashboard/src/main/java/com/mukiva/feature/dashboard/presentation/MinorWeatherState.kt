package com.mukiva.feature.dashboard.presentation

import com.mukiva.feature.dashboard.domain.model.ICurrentWeather
import com.mukiva.feature.dashboard.domain.model.ILocation
import com.mukiva.feature.dashboard.domain.model.IMinimalForecast

data class MinorWeatherState(
    val type: Type,
    val location: ILocation,
    val currentWeather: ICurrentWeather?,
    val minimalForecastState: Collection<IMinimalForecast>
) {
    enum class Type {
        LOADING,
        CONTENT
    }
}