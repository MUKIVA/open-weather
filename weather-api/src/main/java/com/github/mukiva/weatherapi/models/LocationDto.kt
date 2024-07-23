package com.github.mukiva.weatherapi.models

import com.github.mukiva.weatherapi.utils.DateTimeUnixSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class LocationDto(
    @SerialName("id")
    val id: Long = 0,
    @SerialName("name")
    val name: String,
    @SerialName("region")
    val region: String,
    @SerialName("country")
    val country: String,
    @SerialName("url")
    val url: String? = null,
    @SerialName("lat")
    val lat: Double,
    @SerialName("lon")
    val lon: Double,
    @SerialName("tz_id")
    val tzId: String? = null,
    @SerialName("localtime_epoch")
    @Serializable(with = DateTimeUnixSerializer::class)
    val localtimeEpoch: LocalDateTime? = null,
)
