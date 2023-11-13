package com.mukiva.location_search.domain.model

data class Location(
    val uid: Int = 0,
    val cityName: String,
    val regionName: String,
    val countryName: String,
    val isAdded: Boolean
)