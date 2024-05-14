package com.github.mukiva.openweather.core.domain.weather

import com.github.mukiva.openweather.core.domain.settings.UnitsType

data class Distance(
    val unitsType: UnitsType,
    private val km: Double,
    private val miles: Double,
) {
    val value: Double
        get() = when (unitsType) {
            UnitsType.METRIC -> km
            UnitsType.IMPERIAL -> miles
        }
}
