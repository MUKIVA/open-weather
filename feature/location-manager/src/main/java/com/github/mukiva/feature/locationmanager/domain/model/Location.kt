package com.github.mukiva.feature.locationmanager.domain.model

internal data class Location(
    val id: Int = 0,
    val position: Int,
    val cityName: String,
    val regionName: String,
    val countryName: String,
    val isAdded: Boolean
)
