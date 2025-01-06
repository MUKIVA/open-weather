package com.github.mukiva.openweather.core.domain.weather

import com.github.mukiva.openweather.core.domain.settings.UnitsType

private const val MM_HG_IN_ONE_MB = 0.750062f

data class Pressure(
    val unitsType: UnitsType,
    private val pressureMb: Double,
    private val pressureIn: Double,
) {
    val value: Double
        get() = pressureMb * MM_HG_IN_ONE_MB

}
