package com.mukiva.current_weather.domain.model

import java.util.Date

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Float,
    val lon: Float,
    val tzId: String,
    val localtimeEpoch: Int,
    val localtime: Date
)