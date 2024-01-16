package com.mukiva.openweather.glue.location_manager.mapper

import com.mukiva.core.data.repository.location.entity.LocationDTO
import com.mukiva.feature.location_manager.domain.model.Location

object LocationMapper {

    fun asDomain(item: LocationDTO) = with(item) {
        Location(
            uid = uid,
            position = position ?: 0,
            cityName = cityName ?: "Unknown",
            regionName = regionName ?: "Unknown",
            countryName = countryName ?: "Unknown",
            isAdded = false
        )
    }

    fun asDTO(item: Location) = with(item) {
        LocationDTO(
            uid = uid,
            position = position,
            cityName = cityName,
            regionName = regionName,
            countryName = countryName,
            lon = 0.0,
            lat = 0.0
        )
    }
}