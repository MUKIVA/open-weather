package com.github.mukiva.weatherdata.models

public data class ForecastWithCurrentAndLocationData(
    val locationData: LocationData,
    val currentData: CurrentData,
    val forecastData: ForecastData,
)

