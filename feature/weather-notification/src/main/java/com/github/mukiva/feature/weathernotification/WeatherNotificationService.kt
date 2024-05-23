package com.github.mukiva.feature.weathernotification

import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.openweather.core.domain.settings.UnitsType
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.weatherdata.ForecastRepository
import com.github.mukiva.weatherdata.LocationRepository
import com.github.mukiva.weatherdata.SettingsRepository
import com.github.mukiva.weatherdata.models.Location
import com.github.mukiva.weatherdata.utils.RequestResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.github.mukiva.core.ui.R as CoreUiRes

@AndroidEntryPoint
class WeatherNotificationService : JobService() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var forecastRepository: ForecastRepository

    private val mJob = SupervisorJob()
    private val mServiceScope = CoroutineScope(Dispatchers.IO + mJob)

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i("WeatherNotificationService", "Start Job")
        notifyAboutCurrentWeather()

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i("WeatherNotificationService", "Stop Job")
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun notifyAboutCurrentWeather() = mServiceScope.launch {
        val lang = settingsRepository.getLocalization()
            .first()
        Log.d("WeatherNotificationService", "LANG: Received")
        val unitsType = settingsRepository.getUnitsType()
            .first()
        Log.d("WeatherNotificationService", "UNITS_TYPE: Received")
        locationRepository.getAllLocal()
            .filterIsInstance<RequestResult.Success<List<Location>>>()
            .map { requestResult -> checkNotNull(requestResult.data) }
            .onEach { Log.d("WeatherNotificationService", "GetLocation-map: $it") }
            .filter { locations -> locations.isNotEmpty() }
            .onEach { Log.d("WeatherNotificationService", "GetLocation-map: $it") }
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
//        sendNotification("Test", Temp(UnitsType.METRIC, 10.0, 50.0))
    }

    private fun sendNotification(
        locationName: String,
        currentTemp: Temp
    ) {
        val temp = applicationContext.getTempString(currentTemp)
        val content = getString(R.string.notification_content_template, locationName, temp)

        val notification = NotificationCompat
            .Builder(applicationContext, WeatherNotificationChannelHolder.CURRENT_WEATHER_CHANNEL_ID)
            .setSmallIcon(CoreUiRes.drawable.ic_launcher_foreground)
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
