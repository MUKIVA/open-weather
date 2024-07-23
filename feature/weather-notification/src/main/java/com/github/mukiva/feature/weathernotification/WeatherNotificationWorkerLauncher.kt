package com.github.mukiva.feature.weathernotification

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration

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