package com.mukiva.openweather.glue.dashboard.repository

import com.mukiva.feature.dashboard.domain.repository.IForecastUpdater
import com.mukiva.openweather.glue.TimeForecastUpdater
import javax.inject.Inject

class AdapterTimeForecastUpdater @Inject constructor(
    private val timeForecastUpdater: TimeForecastUpdater
) : IForecastUpdater {
    override fun observeUpdate(block: () -> Unit) {
        timeForecastUpdater.observeUpdate(block)
    }

}