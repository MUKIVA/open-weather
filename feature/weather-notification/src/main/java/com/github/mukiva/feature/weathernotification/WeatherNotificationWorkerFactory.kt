package com.github.mukiva.feature.weathernotification

import android.content.Context
import androidx.work.WorkerParameters
import com.github.mukiva.weatherdata.IForecastRepository
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import javax.inject.Inject

class WeatherNotificationWorkerFactory @Inject constructor(
    private val settingsRepository: ISettingsRepository,
    private val locationRepository: ILocationRepository,
    private val forecastRepository: IForecastRepository
) {
    fun create(
        applicationContext: Context,
        workerParameters: WorkerParameters,
    ) = WeatherNotificationWorker(
        applicationContext,
        workerParameters,
        settingsRepository,
        locationRepository,
        forecastRepository
    )
}