package com.github.mukiva.open_weather.core.domain.settings

sealed class Group {
    data class General(
        val theme: Theme,
        val unitsType: UnitsType
    ) : Group()
}