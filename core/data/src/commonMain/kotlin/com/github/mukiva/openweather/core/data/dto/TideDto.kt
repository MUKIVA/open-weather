package com.github.mukiva.openweather.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TideDto(
    @SerialName("tide_time")
    val tideTime: String?,
    @SerialName("tide_height_mt")
    val tideHeightMt: Float?,
    @SerialName("tide_type")
    val tideType: String?
)