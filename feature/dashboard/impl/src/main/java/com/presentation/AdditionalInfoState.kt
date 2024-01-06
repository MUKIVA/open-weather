package com.presentation

import com.domain.model.CurrentWeather
import com.mukiva.api.SpeedUnitsType
import com.mukiva.api.TempUnitsType

data class AdditionalInfoState(
    val type: Type,
    val position: Int,
    val currentWeather: CurrentWeather?,
    val tempUnitsType: TempUnitsType,
    val speedUnitsType: SpeedUnitsType
) {
    enum class Type {
        LOADING,
        CONTENT
    }

    companion object {
        fun default() = AdditionalInfoState(
            type = Type.LOADING,
            position = 0,
            currentWeather = null,
            tempUnitsType = TempUnitsType.CELSIUS,
            speedUnitsType = SpeedUnitsType.METRIC
        )
    }
}