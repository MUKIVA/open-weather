package com.github.mukiva.weatherapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DayDto(
    @SerialName("maxtemp_c")
    val maxTempC: Double,
    @SerialName("maxtemp_f")
    val maxTempF: Double,
    @SerialName("mintemp_c")
    val minTempC: Double,
    @SerialName("mintemp_f")
    val minTempF: Double,
    @SerialName("avgtemp_c")
    val avgTempC: Double,
    @SerialName("avgtemp_f")
    val avgTempF: Double,
    @SerialName("maxwind_mph")
    val maxWindMph: Double,
    @SerialName("maxwind_kph")
    val maxWindKph: Double,
    @SerialName("totalprecip_mm")
    val totalPrecipMm: Double,
    @SerialName("totalprecip_in")
    val totalPrecipIn: Double,
    @SerialName("totalsnow_cm")
    val totalSnowCm: Double,
    @SerialName("avgvis_km")
    val avgVisKm: Double,
    @SerialName("avgvis_miles")
    val avgVisMiles: Double,
    @SerialName("avghumidity")
    val avgHumidity: Int,
    @SerialName("daily_will_it_rain")
    val dailyWillItRain: Int,
    @SerialName("daily_chance_of_rain")
    val dailyChanceOfRain: Int,
    @SerialName("daily_will_it_snow")
    val dailyWillItSnow: Int,
    @SerialName("daily_chance_of_snow")
    val dailyChanceOfSnow: Int,
    @SerialName("uv")
    val uv: Double,
    @SerialName("condition")
    val condition: ConditionDto
)
