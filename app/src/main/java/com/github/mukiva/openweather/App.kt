package com.github.mukiva.openweather

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.github.mukiva.feature.weathernotification.WeatherNotificationChannelHolder
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var weatherNotificationChannelHolder: WeatherNotificationChannelHolder

    @Inject
    lateinit var workerFactory: MainWorkerFactory

    override fun onCreate() {
        super.onCreate()
        weatherNotificationChannelHolder.initChannels()
        WorkManager.initialize(this, workManagerConfiguration)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.ERROR)
            .setWorkerFactory(workerFactory)
            .build()
}
