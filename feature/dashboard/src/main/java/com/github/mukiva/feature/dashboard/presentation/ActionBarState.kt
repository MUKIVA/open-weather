package com.github.mukiva.feature.dashboard.presentation

import com.github.mukiva.openweather.core.domain.weather.Temp

sealed interface IActionBarState {
    data object Loading : IActionBarState
    data class Content(
        val location: String,
        val imageCode: Int,
        val isDay: Boolean,
        val temp: Temp
    ) : IActionBarState
}