package com.github.mukiva.openweather.core.domain.weather

import com.github.mukiva.openweather.core.domain.settings.UnitsType

data class Pressure(
    val unitsType: UnitsType,
    private val pressureMb: Double,
    private val pressureIn: Double,
) {
    val value: Double
        get() = when (unitsType) {
            UnitsType.METRIC -> pressureMb
            UnitsType.IMPERIAL -> pressureIn
        }
}
