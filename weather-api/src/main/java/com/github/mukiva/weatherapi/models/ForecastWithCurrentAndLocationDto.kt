package com.github.mukiva.weatherapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastWithCurrentAndLocationDto(
    @SerialName("location")
    val location: LocationDto,
    @SerialName("current")
    val current: CurrentDto,
    @SerialName("forecast")
    val forecast: ForecastDto,
)
