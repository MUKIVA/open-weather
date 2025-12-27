package com.github.mukiva.openweather.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDayDto(
    @SerialName("date")
    val date: String?,
    @SerialName("date_epoch")
    val dateEpoch: Int?,
    @SerialName("day")
    val day: DayDto?,
    @SerialName("astro")
    val astro: AstroDto?,
    @SerialName("air_quality")
    val airQuality: AirQualityDto?,
    @SerialName("hour")
    val hour: HourDto?
)