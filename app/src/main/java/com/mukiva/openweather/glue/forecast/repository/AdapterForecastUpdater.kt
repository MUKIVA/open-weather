package com.mukiva.openweather.glue.forecast.repository

import com.mukiva.feature.forecast.domain.repository.IForecastUpdater
import com.mukiva.openweather.glue.TimeForecastUpdater
import javax.inject.Inject

class AdapterForecastUpdater @Inject constructor(
    private val updater: TimeForecastUpdater
) : IForecastUpdater {
    override fun observeUpdate(block: () -> Unit) {
        updater.observeUpdate(block)
    }
}