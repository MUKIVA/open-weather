package com.mukiva.openweather.glue.forecast.di

import com.mukiva.feature.forecast.domain.repository.IForecastRepository
import com.mukiva.feature.forecast.domain.repository.IForecastUpdater
import com.mukiva.feature.forecast.navigation.IForecastRouter
import com.mukiva.openweather.glue.forecast.navigation.ForecastRouter
import com.mukiva.openweather.glue.forecast.repository.AdapterForecastRepository
import com.mukiva.openweather.glue.forecast.repository.AdapterForecastUpdater
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IGlueForecastBinds {

    @Binds
    fun bindForecastRouter(
        router: ForecastRouter
    ): IForecastRouter
    @Binds
    fun bindForecastRepository(
        repo: AdapterForecastRepository
    ): IForecastRepository

    @Binds
    fun bindForecastUpdater(
        updater: AdapterForecastUpdater
    ): IForecastUpdater

}