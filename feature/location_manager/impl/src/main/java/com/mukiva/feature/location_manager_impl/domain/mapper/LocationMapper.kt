package com.mukiva.feature.location_manager_impl.domain.mapper

import com.mukiva.feature.location_manager_api.dto.LocationDTO
import com.mukiva.feature.location_manager_impl.domain.model.Location

object LocationMapper {

    fun LocationDTO.asDomain() = run {
        Location(
            uid = uid,
            position = position ?: 0,
            cityName = cityName ?: "Unknown",
            regionName = regionName ?: "Unknown",
            countryName = countryName ?: "Unknown",
            isAdded = false
        )
    }

    fun Location.asDTO() = run {
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