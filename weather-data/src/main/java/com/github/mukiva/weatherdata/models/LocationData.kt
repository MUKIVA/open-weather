package com.github.mukiva.weatherdata.models

import kotlinx.datetime.LocalDateTime

public data class LocationData(
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