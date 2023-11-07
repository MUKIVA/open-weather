package com.mukiva.openweather.domain

import java.time.LocalDateTime

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)

enum class WindDirection {
    WSW
}

data class CurrentWeather(
    val lastUpdatedEpoch: Int,
    val lastUpdated: LocalDateTime,
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
    val gustKph: Float
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Float,
    val lon: Float,
    val tzId: String,
    val localtimeEpoch: Int,
    val localtime: LocalDateTime
)

enum class MoonPhase {
    LAST_QUARTER
}

data class Astro(
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime,
    val moonrise: LocalDateTime,
    val moonSet: LocalDateTime,
    val moonPhase: MoonPhase,
    val moonIllumination: Int,
    val isMoonUp: Int,
    val isSunUp: Int
)

