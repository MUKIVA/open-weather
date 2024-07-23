package com.github.mukiva.weatherapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ForecastDto(
    @SerialName("forecastday")
    val forecastDay: List<ForecastDayDto>,
)

