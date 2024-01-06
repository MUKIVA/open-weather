package com.mukiva.feature.dashboard_impl.domain.model

data class CurrentWithLocation(
    val currentWeather: CurrentWeather?,
    val location: Location
)