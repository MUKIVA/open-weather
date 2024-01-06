package com.presentation

import com.domain.model.CurrentWeather
import com.mukiva.api.UnitsType

data class AdditionalInfoState(
    val type: Type,
    val position: Int,
    val currentWeather: CurrentWeather?,
    val unitsType: UnitsType
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
            unitsType = UnitsType.METRIC
        )
    }
}