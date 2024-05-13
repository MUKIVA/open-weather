package com.github.mukiva.open_weather.core.domain.weather

import com.github.mukiva.open_weather.core.domain.settings.UnitsType

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
