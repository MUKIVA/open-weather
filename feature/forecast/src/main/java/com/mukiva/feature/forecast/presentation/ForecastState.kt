package com.mukiva.feature.forecast.presentation

data class ForecastState(
    val type: Type,
    val dayForecastState: List<DayForecastState>
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
            dayForecastState = emptyList()
        )
    }
}