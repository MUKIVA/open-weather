package com.mukiva.feature.dashboard.ui.adapter

import com.mukiva.feature.dashboard.domain.model.CurrentWeather
import kotlinx.coroutines.flow.StateFlow

interface ICurrentWeatherProvider {

    val currentStateFlow: StateFlow<Current?>

    data class Current(
        val locationName: String,
        val currentWeather: CurrentWeather
    )
}
