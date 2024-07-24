package com.github.mukiva.feature.dashboard.domain.model

import com.github.mukiva.openweather.core.domain.settings.UnitsType
import com.github.mukiva.weatherdata.models.ForecastWithCurrentAndLocationData

internal data class ForecastDataWrapper(
    val errorType: ErrorType,
    val unitsType: UnitsType,
    val data: ForecastWithCurrentAndLocationData? = null
) {
    enum class ErrorType {
        NOTHING,
        GET_LOCATION_ERROR,
        LOCATION_NOT_FOUND,
    }
}