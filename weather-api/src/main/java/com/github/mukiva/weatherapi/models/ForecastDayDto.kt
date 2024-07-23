package com.github.mukiva.weatherapi.models

import com.github.mukiva.weatherapi.utils.DateTimeUnixSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ForecastDayDto(
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

