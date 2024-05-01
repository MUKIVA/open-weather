package com.mukiva.feature.dashboard.domain.model

import java.util.Date

data class CurrentWeather(
    val lastUpdatedEpoch: Int,
    val lastUpdated: Date,
    val tempC: Float,
    val tempF: Float,
    val isDay: Boolean,
    val condition: Condition,
    val windMph: Float,
    val windKph: Float,
    val windDegree: Int,
    val windDir: WindDirection,
    val pressureMb: Float,
    val pressureIn: Float,
    val precipMm: Float,
    val precipIn: Float,
    val humidity: Int,
    val cloud: Int,
    val feelsLikeC: Float,
    val feelsLikeF: Float,
    val visKm: Float,
    val visMiles: Float,
    val uv: Float,
    val gustMph: Float,
    val gustKph: Float,
)