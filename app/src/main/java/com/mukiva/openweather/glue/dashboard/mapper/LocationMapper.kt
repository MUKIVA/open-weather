package com.mukiva.openweather.glue.dashboard.mapper

import com.mukiva.core.data.entity.LocationRemote
import com.mukiva.core.data.repository.location.entity.LocationDTO
import com.mukiva.feature.dashboard.domain.model.ILocation

object LocationMapper {

    fun mapToDomain(item: LocationRemote): ILocation {
        return object : ILocation {
            override val name: String = item.name ?: "UNKNOWN"
            override val region: String = item.region ?: "UNKNOWN"
            override val country: String = item.country ?: "UNKNOWN"
            override val lat: Float = item.lat?.toFloat() ?: 0.0f
            override val lon: Float = item.lon?.toFloat() ?: 0.0f
            override val position: Int = 0

        }
    }

    fun mapToDomain(item: LocationDTO): ILocation {
        return object : ILocation {
            override val name: String = item.cityName ?: "UNKNOWN"
            override val region: String = item.regionName ?: "UNKNOWN"
            override val country: String = item.countryName ?: "UNKNOWN"
            override val lat: Float = item.lat?.toFloat() ?: 0.0f
            override val lon: Float = item.lon?.toFloat() ?: 0.0f
            override val position: Int = item.position ?: 0
        }
    }

}