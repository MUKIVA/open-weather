package com.github.mukiva.open_weather.core.domain

data class Pressure(
    val unitsType: UnitsType,
    private val pressureMb: Double,
    private val pressureIn: Double,
) {
    val value: Double
        get() = when(unitsType) {
            UnitsType.METRIC -> pressureMb
            UnitsType.IMPERIAL -> pressureIn
        }
}