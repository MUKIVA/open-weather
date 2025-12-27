package com.github.mukiva.openweather.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDto(
    @SerialName("last_updated")
    val lastUpdated: String?,
    @SerialName("last_updated_epoch")
    val lastUpdatedEpoch: Int?,
    @SerialName("temp_c")
    val tempC: Double?,
    @SerialName("temp_f")
    val tempF: Double?,
    @SerialName("feelsLikeC")
    val feelsLikeC: Double?,
    @SerialName("feelsLikeF")
    val feelsLikeF: Double?,
    @SerialName("windchill_c")
    val windchillC: Double?,
    @SerialName("windchill_f")
    val windchillF: Double?,
    @SerialName("heatindex_c")
    val healthIndexC: Double?,
    @SerialName("heatindex_f")
    val healthIndexF: Double?,
    @SerialName("dewpoint_c")
    val dewpointC: Double?,
    @SerialName("dewpoint_f")
    val dewpointF: Double?,
    @SerialName("condition")
    val condition: ConditionDto?,
    @SerialName("wind_mph")
    val windMph: Double?,
    @SerialName("wind_kph")
    val windKph: Double?,
    @SerialName("wind_degree")
    val windDegree: Int?,
    @SerialName("wind_dir")
    val windDir: String?,
    @SerialName("pressure_mb")
    val pressureMb: Double?,
    @SerialName("pressure_in")
    val pressureIn: Double?,
    @SerialName("precip_mm")
    val precipMm: Double?,
    @SerialName("precip_in")
    val precipIn: Double?,
    @SerialName("humidity")
    val humidity: Int?,
    @SerialName("cloud")
    val cloud: Int?,
    @SerialName("is_day")
    val isDay: Int?,
    @SerialName("uv")
    val uv: Double?,
    @SerialName("gust_mph")
    val gustMph: Double?,
    @SerialName("gust_kph")
    val gustKph: Double?,
    @SerialName("short_rad")
    val shortRad: Double?,
    @SerialName("diff_rad")
    val diffRad: Double?,
    @SerialName("dni")
    val dni: Double?,
    @SerialName("gti")
    val gti: Double?
)