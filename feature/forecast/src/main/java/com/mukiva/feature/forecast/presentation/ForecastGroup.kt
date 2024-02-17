package com.mukiva.feature.forecast.presentation

import com.mukiva.feature.forecast.domain.IForecastGroup
import com.mukiva.feature.forecast.domain.IForecastItem

data class ForecastGroup<T : IForecastItem>(
    val type: GroupType,
    val forecast: IForecastGroup<T>
) : IForecastGroup<T> by forecast {

    enum class GroupType {
        TEMP,
        WIND,
        PRESSURE,
        HUMIDITY
    }

}