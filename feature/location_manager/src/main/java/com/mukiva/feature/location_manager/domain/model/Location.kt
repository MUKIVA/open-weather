package com.mukiva.feature.location_manager.domain.model

data class Location(
    val uid: Int = 0,
    val position: Int,
    val cityName: String,
    val regionName: String,
    val countryName: String,
    val isAdded: Boolean
)