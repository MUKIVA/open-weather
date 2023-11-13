package com.mukiva.location_search.domain.mapper

import com.mukiva.location_search.data.LocationLocalEntity
import com.mukiva.location_search.data.LocationRemoteEntity
import com.mukiva.location_search.domain.model.Location

object LocationMapper {

    fun LocationRemoteEntity.asDomain(
        localEntity: LocationLocalEntity?
    ) = run {
        Location(
            uid = id ?: 0,
            cityName = name ?: "Unknown",
            regionName = region ?: "Unknown",
            countryName = country ?: "Unknown",
            isAdded = localEntity != null
        )
    }

    fun LocationLocalEntity.asDomain() = run {
        Location(
            uid = uid,
            cityName = cityName ?: "Unknown",
            regionName = regionName ?: "Unknown",
            countryName = countryName ?: "Unknown",
            isAdded = true
        )
    }

    fun Location.asLocalEntity() = run {
        LocationLocalEntity(
            uid = uid,
            cityName = cityName,
            regionName = regionName,
            countryName = countryName
        )
    }
}