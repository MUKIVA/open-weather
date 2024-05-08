package com.mukiva.openweather.glue.dashboard.mapper

import com.mukiva.core.data.entity.LocationRemote
import com.mukiva.core.data.repository.location.entity.LocationDTO
import com.mukiva.feature.dashboard.domain.model.Location

object LocationMapper {

    fun mapToDomain(item: LocationRemote): Location {
        return Location(
            id = item.id.toLong(),
            name = item.name ?: "UNKNOWN",
            region = item.region ?: "UNKNOWN",
            country = item.country ?: "UNKNOWN",
            position = 0,
        )
    }

    fun mapToDomain(item: LocationDTO): Location {
        return Location(
            id = item.uid.toLong(),
            name = item.cityName ?: "UNKNOWN",
            region = item.regionName ?: "UNKNOWN",
            country = item.countryName ?: "UNKNOWN",
            position = item.position ?: 0,
        )
    }

}