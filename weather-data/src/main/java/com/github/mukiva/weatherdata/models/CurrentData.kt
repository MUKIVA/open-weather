package com.github.mukiva.weatherdata.models

import kotlinx.datetime.LocalDateTime

public data class CurrentData(
    var lastUpdatedEpoch: LocalDateTime,
    var tempC: Double,
    var tempF: Double,
    var isDay: Int,
    var conditionData: ConditionData,
    var windMph: Double,
    var windKph: Double,
    var windDegree: Int,
    var windDir: String,
    var pressureMb: Double,
    var pressureIn: Double,
    var precipMm: Double,
    var precipIn: Double,
    var humidity: Int,
    var cloud: Int,
    var feelslikeC: Double,
    var feelslikeF: Double,
    var visKm: Double,
    var visMiles: Double,
    var uv: Double,
    var gustMph: Double,
    var gustKph: Double,
)
