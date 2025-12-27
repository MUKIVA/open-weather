package com.github.mukiva.openweather.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AirQualityDto(
    @SerialName("co")
    val co: Float?,
    @SerialName("o3")
    val o3: Float?,
    @SerialName("no2")
    val no2: Float?,
    @SerialName("so2")
    val so2: Float?,
    @SerialName("pm2_5")
    val pm25: Float?,
    @SerialName("pm10")
    val pm10: Float?,
    @SerialName("us-epa-index")
    val usEpaIndex: Int?,
    @SerialName("gb-defra-index")
    val gbDefraIndex: Int?
)