package com.presentation

import com.domain.model.CurrentWithLocation
import com.mukiva.api.TempUnitsType

data class DashboardState(
    val type: Type,
    val currentIndex: Int,
    val currentWeather: CurrentWithLocation?,
    val locationCount: Int,
    val tempUnitsType: TempUnitsType
) {

    enum class Type {
        INIT,
        LOADING,
        CONTENT,
        EMPTY,
        ERROR
    }

    companion object {
        fun default() = DashboardState(
            type = Type.INIT,
            currentIndex = 0,
            locationCount = 0,
            currentWeather = null,
            tempUnitsType = TempUnitsType.CELSIUS
        )
    }
}