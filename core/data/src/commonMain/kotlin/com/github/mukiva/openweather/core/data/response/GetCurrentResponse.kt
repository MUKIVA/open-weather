package com.github.mukiva.openweather.core.data.response

import com.github.mukiva.openweather.core.data.dto.CurrentWeatherDto
import com.github.mukiva.openweather.core.data.dto.LocationDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCurrentResponse(
    @SerialName("location")
    val location: LocationDto?,
    @SerialName("current")
    val current: CurrentWeatherDto?
)