package com.github.mukiva.feature.weathernotification

import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.weatherdata.IForecastRepository
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.models.Location
import com.github.mukiva.weatherdata.utils.RequestResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.Duration

@HiltWorker
class WeatherNotificationWorker @AssistedInject internal constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val settingsRepository: ISettingsRepository,
    private val locationRepository: ILocationRepository,
    private val forecastRepository: IForecastRepository
) : CoroutineWorker(applicationContext, workerParameters) {

    @AssistedFactory
    interface Factory {
        fun create(
            appContext: Context,
            workerParameters: WorkerParameters
        ): WeatherNotificationWorker
    }

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
            .filterIsInstance<RequestResult.Success<List<Location>>>()
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
                        locationName = data.location.name,
                        currentTemp = Temp(unitsType, data.current.tempC, data.current.tempF)
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

internal class WeatherNotificationWorkerLauncher(
    private val applicationContext: Context
) : IWeatherNotificationServiceLauncher {

    override fun startService() {

        val context = assertApplicationContext(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .setRequiresDeviceIdle(false)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<WeatherNotificationWorker>(
            Duration.ofMinutes(WORK_EXECUTE_DURATION)
        )
            .setConstraints(constraints)
            .setInitialDelay(Duration.ZERO)
            .build()


        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                WEATHER_NOTIFICATION_WORK_ID,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

    override fun stopService() {
        val context = assertApplicationContext(applicationContext)

        WorkManager.getInstance(context)
            .cancelUniqueWork(WEATHER_NOTIFICATION_WORK_ID)
    }

    private fun assertApplicationContext(context: Context): Context {
        return context.applicationContext
            ?: error("It is not the applicationContext")
    }

    companion object {
        private const val WEATHER_NOTIFICATION_WORK_ID = "WEATHER_NOTIFICATION_WORK_ID"
        private const val WORK_EXECUTE_DURATION = 30L
    }
}

fun createWeatherNotificationLauncher(
    applicationContext: Context
): IWeatherNotificationServiceLauncher {
    return WeatherNotificationWorkerLauncher(applicationContext)
}