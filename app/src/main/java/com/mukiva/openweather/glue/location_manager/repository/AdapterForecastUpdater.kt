package com.mukiva.openweather.glue.location_manager.repository

import com.mukiva.feature.location_manager.domain.repository.IForecastUpdater
import com.mukiva.openweather.glue.TimeForecastUpdater
import javax.inject.Inject

class AdapterForecastUpdater @Inject constructor(
    private val updater: TimeForecastUpdater
) : IForecastUpdater {
    override fun markForUpdate() {
        updater.markForUpdate()
    }
}