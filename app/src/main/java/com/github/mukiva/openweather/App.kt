package com.github.mukiva.openweather

import android.app.Application
import com.github.mukiva.feature.weathernotification.WeatherNotificationChannelHolder
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var weatherNotificationChannelHolder: WeatherNotificationChannelHolder

    override fun onCreate() {
        super.onCreate()
        weatherNotificationChannelHolder.initChannels()
    }
}
