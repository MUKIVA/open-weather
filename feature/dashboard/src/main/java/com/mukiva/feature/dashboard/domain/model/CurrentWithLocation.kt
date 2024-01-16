package com.mukiva.feature.dashboard.domain.model

data class CurrentWithLocation(
    val currentWeather: CurrentWeather?,
    val location: Location
)