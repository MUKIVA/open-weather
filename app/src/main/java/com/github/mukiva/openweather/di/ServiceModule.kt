package com.github.mukiva.openweather.di

import android.content.Context
import com.github.mukiva.feature.weathernotification.createNotificationChannelHolder
import com.github.mukiva.feature.weathernotification.createWeatherNotificationLauncher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class ServiceModule {

    @Provides
    fun provideWeatherNotificationServiceLauncher(
        @ApplicationContext context: Context
    ) = createWeatherNotificationLauncher(context)

    @Provides
    fun provideWeatherNotificationChannelHolder(
        @ApplicationContext context: Context
    ) = createNotificationChannelHolder(context)
}
