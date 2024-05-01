package com.github.mukiva.weather_data.models

data class ForecastWithCurrentAndLocation(
    val location: Location,
    val current: Current,
    val forecast: Forecast,
)

