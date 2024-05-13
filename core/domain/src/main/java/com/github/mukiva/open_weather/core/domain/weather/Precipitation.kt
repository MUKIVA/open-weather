package com.github.mukiva.open_weather.core.domain.weather

import com.github.mukiva.open_weather.core.domain.settings.UnitsType

data class Precipitation(
    val unitsType: UnitsType,
    private val mm: Double,
    private val inch: Double,
) {
    val value
        get() = when (unitsType) {
            UnitsType.METRIC -> mm
            UnitsType.IMPERIAL -> inch
        }
}
