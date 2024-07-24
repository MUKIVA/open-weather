package com.github.mukiva.weatherdata.models

public data class AstroData(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val moonPhase: String,
    val moonIllumination: Int,
    val isMoonUp: Int,
    val isSunUp: Int,
)
