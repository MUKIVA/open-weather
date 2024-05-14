package com.github.mukiva.openweather.core.domain.settings

sealed class Group {
    data class General(
        val theme: Theme,
        val unitsType: UnitsType
    ) : Group()
}