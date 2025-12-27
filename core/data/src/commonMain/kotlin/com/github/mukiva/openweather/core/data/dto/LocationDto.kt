package com.github.mukiva.openweather.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LocationDto(
    @SerialName("id")
    val id: Long?,
    @SerialName("lat")
    val lat: Double?,
    @SerialName("lon")
    val lon: Double?,
    @SerialName("name")
    val name: String?,
    @SerialName("region")
    val region: String?,
    @SerialName("country")
    val country: String?,
    @SerialName("tz_id")
    val timeZoneId: String?,
    @SerialName("localtime_epoch")
    val localTimeEpoch: Int?,
    @SerialName("localtime")
    val localTime: String?
)