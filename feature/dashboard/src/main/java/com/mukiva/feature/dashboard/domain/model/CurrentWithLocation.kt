package com.mukiva.feature.dashboard.domain.model

data class CurrentWithLocation(
    val currentWeather: CurrentWeather?,
    val forecastState: Collection<MinimalForecast>,
    val location: Location
)