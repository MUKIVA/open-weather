package com.github.mukiva.openweather.core.domain.weather

import com.github.mukiva.openweather.core.domain.settings.UnitsType

data class Temp(
    val unitsType: UnitsType,
    private val tempC: Double,
    private val tempF: Double,
) {
    val value: Double
        get() = when (unitsType) {
            UnitsType.METRIC -> tempC
            UnitsType.IMPERIAL -> tempF
        }
}
