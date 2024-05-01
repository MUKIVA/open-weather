package com.github.mukiva.weather_api.models

import com.github.mukiva.weather_api.utils.DateTimeUnixSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDayDto(
    @SerialName("date_epoch")
    @Serializable(with = DateTimeUnixSerializer::class)
    val dateEpoch: LocalDateTime,
    @SerialName("day")
    val day: DayDto,
    @SerialName("astro")
    val astro: AstroDto,
    @SerialName("hour")
    val hour: List<HourDto>,
)

