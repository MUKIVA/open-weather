package com.mukiva.feature.dashboard.domain.model

import kotlinx.datetime.LocalDateTime


data class CurrentWeather(
    val lastUpdatedEpoch: LocalDateTime,
    val tempC: Double,
    val tempF: Double,
    val isDay: Boolean,
    val condition: Condition,
    val windMph: Double,
    val windKph: Double,
    val windDegree: Int,
    val windDir: WindDirection,
    val pressureMb: Double,
    val pressureIn: Double,
    val precipMm: Double,
    val precipIn: Double,
    val humidity: Int,
    val cloud: Int,
    val feelsLikeC: Double,
    val feelsLikeF: Double,
    val visKm: Double,
    val visMiles: Double,
    val uv: Double,
    val gustMph: Double,
    val gustKph: Double,
)