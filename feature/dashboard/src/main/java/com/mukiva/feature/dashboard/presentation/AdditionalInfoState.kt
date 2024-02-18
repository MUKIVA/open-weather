package com.mukiva.feature.dashboard.presentation

import com.mukiva.feature.dashboard.domain.model.CurrentWeather
import com.mukiva.feature.dashboard.domain.model.IMinimalForecast
import com.mukiva.feature.dashboard.domain.model.UnitsType

data class AdditionalInfoState(
    val type: Type,
    val position: Int,
    val currentWeather: CurrentWeather?,
    val forecastListState: Collection<IMinimalForecast>,
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
            unitsType = UnitsType.METRIC,
            forecastListState = emptyList()
        )
    }
}