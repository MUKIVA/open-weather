package com.mukiva.core.data.repository.location.entity

data class LocationDTO(
    val uid: Int,
    val position: Int?,
    val cityName: String?,
    val regionName: String?,
    val lon: Double?,
    val lat: Double?,
    val countryName: String?
)