package com.github.mukiva.open_weather.core.domain.weather

import com.github.mukiva.open_weather.core.domain.settings.UnitsType

data class Speed(
    val unitsType: UnitsType,
    private val kph: Double,
    private val mph: Double,
) {
    val value: Double
        get() = when (unitsType) {
            UnitsType.METRIC -> kph
            UnitsType.IMPERIAL -> mph
        }
}
