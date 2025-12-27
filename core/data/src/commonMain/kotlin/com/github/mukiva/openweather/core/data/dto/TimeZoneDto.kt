package com.github.mukiva.openweather.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeZoneDto(
    @SerialName("tz_id")
    val timeZoneId: String?,
    @SerialName("localtime_epoch")
    val localTimeEpoch: Int?,
    @SerialName("localtime")
    val localTime: String?
)