package com.github.mukiva.weather_data.models

import kotlinx.datetime.LocalDateTime

data class Location(
    val id: Long,
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tzId: String,
    val localtimeEpoch: LocalDateTime,
    val priority: Int,
)