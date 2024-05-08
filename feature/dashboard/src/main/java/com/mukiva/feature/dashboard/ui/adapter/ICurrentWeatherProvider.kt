package com.mukiva.feature.dashboard.ui.adapter

import com.mukiva.feature.dashboard.domain.model.CurrentWeather
import com.mukiva.feature.dashboard.domain.model.UnitsType
import kotlinx.coroutines.flow.StateFlow

interface ICurrentWeatherProvider {

    val currentStateFlow: StateFlow<Current?>

    data class Current(
        val locationName: String,
        val unitsType: UnitsType,
        val currentWeather: CurrentWeather
    )

}