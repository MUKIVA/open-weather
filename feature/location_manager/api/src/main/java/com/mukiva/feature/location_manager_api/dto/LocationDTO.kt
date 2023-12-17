package com.mukiva.feature.location_manager_api.dto

data class LocationDTO(
    val uid: Int,
    val cityName: String?,
    val regionName: String?,
    val lon: Double?,
    val lat: Double?,
    val countryName: String?
)