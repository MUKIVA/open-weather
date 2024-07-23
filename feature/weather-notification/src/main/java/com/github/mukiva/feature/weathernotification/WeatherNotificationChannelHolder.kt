package com.github.mukiva.feature.weathernotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

interface INotificationChannelHolder {
    fun initChannels()
}

class WeatherNotificationChannelHolder internal constructor(
    private val applicationContext: Context
): INotificationChannelHolder {

    private val mNotificationManager by lazy {
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun initChannels() {
        initCurrentWeatherChannel()
    }

    private fun initCurrentWeatherChannel() {
        val channel = NotificationChannel(
            CURRENT_WEATHER_CHANNEL_ID,
            applicationContext.getString(R.string.notification_title),
            NotificationManager.IMPORTANCE_LOW
        )
        mNotificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CURRENT_WEATHER_CHANNEL_ID = "current_weather_notification"
    }
}

fun createNotificationChannelHolder(
    applicationContext: Context
): INotificationChannelHolder = WeatherNotificationChannelHolder(applicationContext)
