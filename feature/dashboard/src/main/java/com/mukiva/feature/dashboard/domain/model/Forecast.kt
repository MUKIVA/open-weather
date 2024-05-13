package com.mukiva.feature.dashboard.domain.model

data class Forecast(
    val currentWeather: CurrentWeather,
    val forecastState: List<MinimalForecast>,
)