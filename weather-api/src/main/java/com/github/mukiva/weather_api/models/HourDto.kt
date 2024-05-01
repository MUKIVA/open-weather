package com.github.mukiva.weather_api.models

import com.github.mukiva.weather_api.utils.DateTimeUnixSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourDto(
    @SerialName("time_epoch")
    @Serializable(with = DateTimeUnixSerializer::class)
    val timeEpoch: LocalDateTime,
    @SerialName("temp_c")
    val tempC: Double,
    @SerialName("temp_f")
    val tempF: Double,
    @SerialName("is_day")
    val isDay: Int,
    @SerialName("wind_mph")
    val windMph: Double,
    @SerialName("wind_kph")
    val windKph: Double,
    @SerialName("wind_degree")
    val windDegree: Int,
    @SerialName("wind_dir")
    val windDir: String,
    @SerialName("pressure_mb")
    val pressureMb: Double,
    @SerialName("pressure_in")
    val pressureIn: Double,
    @SerialName("precip_mm")
    val precipMm: Double,
    @SerialName("precip_in")
    val precipIn: Double,
    @SerialName("snow_cm")
    val snowCm: Double,
    @SerialName("humidity")
    val humidity: Int,
    @SerialName("cloud")
    val cloud: Int,
    @SerialName("feelslike_c")
    val feelsLikeC: Double,
    @SerialName("feelslike_f")
    val feelsLikeF: Double,
    @SerialName("windchill_c")
    val windChillC: Double,
    @SerialName("windchill_f")
    val windChillF: Double,
    @SerialName("heatindex_c")
    val heatIndexC: Double,
    @SerialName("heatindex_f")
    val heatIndexF: Double,
    @SerialName("dewpoint_c")
    val dewPointC: Double,
    @SerialName("dewpoint_f")
    val dewPointF: Double,
    @SerialName("will_it_rain")
    val willItRain: Int,
    @SerialName("chance_of_rain")
    val chanceOfRain: Int,
    @SerialName("will_it_snow")
    val willItSnow: Int,
    @SerialName("chance_of_snow")
    val chanceOfSnow: Int,
    @SerialName("vis_km")
    val visKm: Double,
    @SerialName("vis_miles")
    val visMiles: Double,
    @SerialName("gust_mph")
    val gustMph: Double,
    @SerialName("gust_kph")
    val gustKph: Double,
    @SerialName("uv")
    val uv: Double,
    @SerialName("condition")
    val condition: ConditionDto
)