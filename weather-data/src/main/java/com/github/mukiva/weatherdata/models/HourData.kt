package com.github.mukiva.weatherdata.models

import kotlinx.datetime.LocalDateTime

public data class HourData(
    val timeEpoch: LocalDateTime,
    val tempC: Double,
    val tempF: Double,
    val isDay: Int,
    val windMph: Double,
    val windKph: Double,
    val windDegree: Int,
    val windDir: String,
    val pressureMb: Double,
    val pressureIn: Double,
    val precipMm: Double,
    val precipIn: Double,
    val snowCm: Double,
    val humidity: Int,
    val cloud: Int,
    val feelsLikeC: Double,
    val feelsLikeF: Double,
    val windChillC: Double,
    val windChillF: Double,
    val heatIndexC: Double,
    val heatIndexF: Double,
    val dewPointC: Double,
    val dewPointF: Double,
    val willItRain: Int,
    val chanceOfRain: Int,
    val willItSnow: Int,
    val chanceOfSnow: Int,
    val visKm: Double,
    val visMiles: Double,
    val gustMph: Double,
    val gustKph: Double,
    val uv: Double,
    val conditionData: ConditionData
)