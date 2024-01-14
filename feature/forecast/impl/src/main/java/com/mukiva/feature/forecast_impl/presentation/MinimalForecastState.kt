package com.mukiva.feature.forecast_impl.presentation

import com.mukiva.feature.forecast_impl.domain.Forecast

data class MinimalForecastState(
    val forecastItems: List<Forecast>,
    val type: Type
) {
    enum class Type {
        INIT,
        EMPTY,
        LOADING,
        CONTENT,
        ERROR
    }

   companion object {
       fun default() = MinimalForecastState(
           forecastItems = emptyList(),
           type = Type.INIT
       )
   }

}