package com.domain.mapper

import com.domain.model.Location
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    private val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

    fun com.data.LocationJson.mapToDomain(): Location {
        return Location(
            name = name ?: "UNKNOWN",
            region = region ?: "UNKNOWN",
            country = country ?: "UNKNOWN",
            lat = lat?.toFloat() ?: 0.0f,
            lon = lon?.toFloat() ?: 0.0f,
            tzId = tzId ?: "UNKNOWN",
            localtimeEpoch = localtimeEpoch ?: 0,
            localtime = simpleDateFormat.parse(localtime ?: "")
                ?: throw Exception("Can't parse localtime")
        )
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd HH:mm"
    }

}