package com.mukiva.feature.forecast.presentation

import com.mukiva.feature.forecast.domain.ForecastItem
import kotlinx.datetime.LocalDateTime

sealed class HourlyForecast {
    data object Init : HourlyForecast()

    data class Content(
        val index: Int,
        val date: LocalDateTime,
        val hours: List<ForecastItem>,
    ) : HourlyForecast()
}
