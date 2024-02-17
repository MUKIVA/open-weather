package com.mukiva.openweather.di

import com.mukiva.core.data.repository.IForecastUpdater
import com.mukiva.openweather.glue.TimeForecastUpdater
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindForecastUpdater(
        updater: TimeForecastUpdater
    ): IForecastUpdater

}