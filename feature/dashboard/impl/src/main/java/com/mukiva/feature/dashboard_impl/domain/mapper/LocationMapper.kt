package com.mukiva.feature.dashboard_impl.domain.mapper

import com.mukiva.feature.dashboard_impl.data.LocationRemote
import com.mukiva.feature.dashboard_impl.domain.model.Location
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    fun LocationRemote.mapToDomain(): Location {
        return Location(
            name = name ?: "UNKNOWN",
            region = region ?: "UNKNOWN",
            country = country ?: "UNKNOWN",
            lat = lat?.toFloat() ?: 0.0f,
            lon = lon?.toFloat() ?: 0.0f
        )
    }

}