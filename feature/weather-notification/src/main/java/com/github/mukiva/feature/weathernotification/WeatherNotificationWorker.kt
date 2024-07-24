package com.github.mukiva.feature.weathernotification

import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.weatherdata.IForecastRepository
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.models.LocationData
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class WeatherNotificationWorker internal constructor(
    applicationContext: Context,
    workerParameters: WorkerParameters,
    private val settingsRepository: ISettingsRepository,
    private val locationRepository: ILocationRepository,
    private val forecastRepository: IForecastRepository
) : CoroutineWorker(applicationContext, workerParameters) {

    override suspend fun doWork(): Result {
        notifyAboutCurrentWeather()
        return Result.success()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun notifyAboutCurrentWeather() {
        if (!checkNetworkAvailable())
            return
        val lang = settingsRepository.getLocalization()
            .first()
        val unitsType = settingsRepository.getUnitsType()
            .first()
        locationRepository.getAllLocal()
            .filterIsInstance<RequestResult.Success<List<LocationData>>>()
            .map { requestResult -> checkNotNull(requestResult.data) }
            .filter { locations -> locations.isNotEmpty() }
            .flatMapLatest { locations ->
                val id = locations[0].id
                forecastRepository.getForecast(id, lang)
            }
            .filter { requestResult -> requestResult !is RequestResult.InProgress }
            .onEach { requestResult ->
                if (requestResult is RequestResult.Success) {
                    val data = checkNotNull(requestResult.data)
                    sendNotification(
                        locationName = data.locationData.name,
                        currentTemp = Temp(unitsType, data.currentData.tempC, data.currentData.tempF)
                    )
                }
            }
            .first()
    }

    private fun checkNetworkAvailable(): Boolean {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val an = cm.activeNetwork ?: return false
        val actNw = cm.getNetworkCapabilities(an) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun sendNotification(
        locationName: String,
        currentTemp: Temp
    ) = with(applicationContext) {
        val temp = getTempString(currentTemp)
        val content = getString(R.string.notification_content_template, locationName, temp)

        val notification = NotificationCompat
            .Builder(applicationContext, WeatherNotificationChannelHolder.CURRENT_WEATHER_CHANNEL_ID)
            .setSmallIcon(com.github.mukiva.core.ui.R.drawable.ic_launcher_foreground)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentText(content)
            .build()
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 0
    }
}

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

fun createWeatherNotificationLauncher(
    applicationContext: Context
): IWeatherNotificationServiceLauncher {
    return WeatherNotificationWorkerLauncher(applicationContext)
}