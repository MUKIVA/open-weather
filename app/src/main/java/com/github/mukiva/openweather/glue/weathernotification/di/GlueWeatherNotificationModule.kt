package com.github.mukiva.openweather.glue.weathernotification.di

import com.github.mukiva.feature.weathernotification.IWeatherNotificationServiceLauncher
import com.github.mukiva.openweather.service.WeatherNotificationServiceLauncher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GlueWeatherNotificationModule {

    @Provides
    fun provideWeatherNotificationServiceLauncher(
        weatherNotificationServiceLauncher: WeatherNotificationServiceLauncher
    ): IWeatherNotificationServiceLauncher = object : IWeatherNotificationServiceLauncher {
        override fun startService() {
            weatherNotificationServiceLauncher.startService()
        }

        override fun stopService() {
            weatherNotificationServiceLauncher.stopService()
        }
    }
}
