package com.github.mukiva.feature.dashboard.domain.model

data class Forecast(
    val locationName: String,
    val currentWeather: CurrentWeather,
    val forecastState: List<MinimalForecast>,
)
