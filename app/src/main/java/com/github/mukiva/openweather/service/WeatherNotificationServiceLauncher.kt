package com.github.mukiva.openweather.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import com.github.mukiva.feature.weathernotification.WeatherNotificationService

class WeatherNotificationServiceLauncher(
    private val applicationContext: Context,
) {
    fun startService() {
        val componentName =
            ComponentName(applicationContext, WeatherNotificationService::class.java)
        val jobInfo = JobInfo.Builder(WEATHER_NOTIFICATION_JOB_ID, componentName)
            .setPeriodic(JOB_START_PERIOD)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setRequiresCharging(false)
            .build()
        val jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)
    }

    fun stopService() {
        val jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(WEATHER_NOTIFICATION_JOB_ID)
    }

    companion object {
        private const val JOB_START_PERIOD = 30 * 60 * 1000L
        private const val WEATHER_NOTIFICATION_JOB_ID = 0
    }
}
