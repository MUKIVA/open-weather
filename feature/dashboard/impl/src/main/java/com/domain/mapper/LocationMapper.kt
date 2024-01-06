package com.domain.mapper

import com.domain.model.Location
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    fun com.data.LocationRemote.mapToDomain(): Location {
        return Location(
            name = name ?: "UNKNOWN",
            region = region ?: "UNKNOWN",
            country = country ?: "UNKNOWN",
            lat = lat?.toFloat() ?: 0.0f,
            lon = lon?.toFloat() ?: 0.0f
        )
    }

}