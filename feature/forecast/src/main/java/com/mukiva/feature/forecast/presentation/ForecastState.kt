package com.mukiva.feature.forecast.presentation

import com.mukiva.feature.forecast.domain.IHourlyForecast
import com.mukiva.feature.forecast.domain.UnitsType

data class ForecastState(
    val type: Type,
    val hourlyForecast: Collection<IHourlyForecast>,
    val unitsType: UnitsType
) {

    enum class Type {
        INIT,
        ERROR,
        CONTENT,
        LOADING
    }

    companion object {
        fun default() = ForecastState(
            type = Type.INIT,
            hourlyForecast = emptyList(),
            unitsType = UnitsType.METRIC
        )
    }
}