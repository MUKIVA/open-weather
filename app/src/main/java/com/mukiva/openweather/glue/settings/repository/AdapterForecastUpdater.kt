package com.mukiva.openweather.glue.settings.repository

import com.mukiva.feature.settings.domain.repository.IForecastUpdater
import com.mukiva.openweather.glue.TimeForecastUpdater
import javax.inject.Inject

class AdapterForecastUpdater @Inject constructor(
    private val updater: TimeForecastUpdater
) : IForecastUpdater {
    override fun markForUpdate() {
        updater.markForUpdate()
    }
}