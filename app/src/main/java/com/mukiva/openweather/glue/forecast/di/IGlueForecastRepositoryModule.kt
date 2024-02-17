package com.mukiva.openweather.glue.forecast.di

import com.mukiva.feature.forecast.domain.repository.IForecastRepository
import com.mukiva.feature.forecast.navigation.IForecastRouter
import com.mukiva.openweather.glue.forecast.navigation.ForecastRouter
import com.mukiva.openweather.glue.forecast.repository.AdapterForecastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IGlueForecastRepositoryModule {

    @Binds
    fun bindForecastRouter(
        router: ForecastRouter
    ): IForecastRouter
    @Binds
    fun bindForecastRepository(
        repo: AdapterForecastRepository
    ): IForecastRepository

}