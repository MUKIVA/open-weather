package com.mukiva.openweather.glue.dashboard.mapper

import com.mukiva.core.data.entity.LocationRemote
import com.mukiva.core.data.repository.location.entity.LocationDTO
import com.mukiva.feature.dashboard.domain.model.Location

object LocationMapper {

    fun mapToDomain(item: LocationRemote): Location = with(item) {
        return Location(
            name = name ?: "UNKNOWN",
            region = region ?: "UNKNOWN",
            country = country ?: "UNKNOWN",
            lat = lat?.toFloat() ?: 0.0f,
            lon = lon?.toFloat() ?: 0.0f
        )
    }

    fun mapToDomain(item: LocationDTO): Location = with(item) {
        return Location(
            name = cityName ?: "UNKNOWN",
            region = regionName ?: "UNKNOWN",
            country = countryName ?: "UNKNOWN",
            lat = lat?.toFloat() ?: 0.0f,
            lon = lon?.toFloat() ?: 0.0f
        )
    }

}