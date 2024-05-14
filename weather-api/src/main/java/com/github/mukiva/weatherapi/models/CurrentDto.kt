package com.github.mukiva.weatherapi.models

import com.github.mukiva.weatherapi.utils.DateTimeUnixSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentDto(
    @SerialName("last_updated_epoch")
    @Serializable(with = DateTimeUnixSerializer::class)
    var lastUpdatedEpoch: LocalDateTime,
    @SerialName("temp_c")
    var tempC: Double,
    @SerialName("temp_f")
    var tempF: Double,
    @SerialName("is_day")
    var isDay: Int,
    @SerialName("condition")
    var condition: ConditionDto,
    @SerialName("wind_mph")
    var windMph: Double,
    @SerialName("wind_kph")
    var windKph: Double,
    @SerialName("wind_degree")
    var windDegree: Int,
    @SerialName("wind_dir")
    var windDir: String,
    @SerialName("pressure_mb")
    var pressureMb: Double,
    @SerialName("pressure_in")
    var pressureIn: Double,
    @SerialName("precip_mm")
    var precipMm: Double,
    @SerialName("precip_in")
    var precipIn: Double,
    @SerialName("humidity")
    var humidity: Int,
    @SerialName("cloud")
    var cloud: Int,
    @SerialName("feelslike_c")
    var feelslikeC: Double,
    @SerialName("feelslike_f")
    var feelslikeF: Double,
    @SerialName("vis_km")
    var visKm: Double,
    @SerialName("vis_miles")
    var visMiles: Double,
    @SerialName("uv")
    var uv: Double,
    @SerialName("gust_mph")
    var gustMph: Double,
    @SerialName("gust_kph")
    var gustKph: Double,
)
