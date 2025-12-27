package com.github.mukiva.openweather.core.data.response

import com.github.mukiva.openweather.core.data.dto.AlertsDto
import com.github.mukiva.openweather.core.data.dto.CurrentWeatherDto
import com.github.mukiva.openweather.core.data.dto.ForecastDto
import com.github.mukiva.openweather.core.data.dto.LocationDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetForecastResponse(
    @SerialName("location")
    val location: LocationDto?,
    @SerialName("current")
    val current: CurrentWeatherDto?,
    @SerialName("forecast")
    val forecast: ForecastDto?,
    @SerialName("alerts")
    val alerts: AlertsDto?
)