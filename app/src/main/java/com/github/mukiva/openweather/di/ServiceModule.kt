package com.github.mukiva.openweather.di

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerFactory
import com.github.mukiva.feature.weathernotification.WeatherNotificationChannelHolder
import com.github.mukiva.feature.weathernotification.createWeatherNotificationLauncher
import com.github.mukiva.openweather.MainWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun provideWeatherNotificationServiceLauncher(
        @ApplicationContext context: Context
    ) = createWeatherNotificationLauncher(context)

    @Provides
    fun provideWeatherNotificationChannelHolder(
        @ApplicationContext context: Context
    ) = WeatherNotificationChannelHolder(context)
}
