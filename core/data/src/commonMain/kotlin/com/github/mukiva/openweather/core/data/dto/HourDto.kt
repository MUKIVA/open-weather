package com.github.mukiva.openweather.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourDto(
    @SerialName("time_epoch")
    val timeEpoch: Int?,
    @SerialName("time")
    val time: String?,
    @SerialName("temp_c")
    val tempC: Double?,
    @SerialName("temp_f")
    val tempF: Double?,
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
    @SerialName("snow_cm")
    val snowCm: Double?,
    @SerialName("humidity")
    val humidity: Int?,
    @SerialName("cloud")
    val cloud: Int?,
    @SerialName("feelslike_c")
    val feelsLikeC: Double?,
    @SerialName("feelslike_f")
    val feelsLikeF: Double?,
    @SerialName("windchill_c")
    val windChillC: Double?,
    @SerialName("windchill_f")
    val windChillF: Double?,
    @SerialName("heatindex_c")
    val heatIndexC: Double?,
    @SerialName("heatindex_f")
    val heatIndexF: Double?,
    @SerialName("dewpoint_c")
    val dewpointC: Double?,
    @SerialName("dewpoint_f")
    val dewpointF: Double?,
    @SerialName("will_it_rain")
    val willItRain: Int?,
    @SerialName("will_it_snow")
    val willItSnow: Int?,
    @SerialName("is_day")
    val isDay: Int?,
    @SerialName("vis_km")
    val visKm: Double?,
    @SerialName("vis_miles")
    val visMiles: Double?,
    @SerialName("chance_of_rain")
    val chanceOfRain: Int?,
    @SerialName("chance_of_snow")
    val chanceOfSnow: Int?,
    @SerialName("gust_mph")
    val gustMph: Double?,
    @SerialName("gust_kph")
    val gustKph: Double?,
    @SerialName("uv")
    val uv: Double?,
    @SerialName("short_rad")
    val shortRad: Double?,
    @SerialName("diff_rad")
    val diffRad: Double?,
    @SerialName("dni")
    val dni: Double?,
    @SerialName("gti")
    val gti: Double?,
    @SerialName("air_quality")
    val airQuality: AirQualityDto?
)