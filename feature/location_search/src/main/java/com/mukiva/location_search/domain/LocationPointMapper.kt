package com.mukiva.location_search.domain

import com.mukiva.location_search.data.LocationJson
import javax.inject.Inject

class LocationPointMapper @Inject constructor(

) {

    fun LocationJson.toDomain() = run {
        LocationPoint(
            cityName = name ?: "Unknown",
            regionName = region ?: "Unknown",
            countryName = country ?: "Unknown"
        )
    }

}