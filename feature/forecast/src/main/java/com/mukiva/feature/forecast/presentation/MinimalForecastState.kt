package com.mukiva.feature.forecast.presentation

import com.mukiva.feature.forecast.domain.IMinimalForecast

data class MinimalForecastState(
    val forecastItems: List<IMinimalForecast>,
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