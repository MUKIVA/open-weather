package com.mukiva.current_weather.domain.model

data class CurrentWithLocation(
    val currentWeather: CurrentWeather,
    val location: Location
)