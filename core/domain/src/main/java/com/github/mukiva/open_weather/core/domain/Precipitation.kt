package com.github.mukiva.open_weather.core.domain

data class Precipitation(
    val unitsType: UnitsType,
    private val mm: Double,
    private val inch: Double,
) {
    val value
        get() = when(unitsType) {
            UnitsType.METRIC -> mm
            UnitsType.IMPERIAL -> inch
        }
}