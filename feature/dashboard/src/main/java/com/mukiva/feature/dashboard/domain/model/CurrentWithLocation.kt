package com.mukiva.feature.dashboard.domain.model

data class CurrentWithLocation(
    val currentWeather: ICurrentWeather?,
    val forecastState: Collection<IMinimalForecast>,
    val location: ILocation
)