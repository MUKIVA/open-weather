package com.github.mukiva.weatherdata.models

data class ForecastWithCurrentAndLocation(
    val location: Location,
    val current: Current,
    val forecast: Forecast,
)

