package com.mukiva.openweather.glue.forecast.di

import com.mukiva.feature.forecast.domain.repository.IForecastRepository
import com.mukiva.openweather.glue.forecast.repository.AdapterForecastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IGlueForecastRepositoryModule {
    @Binds
    fun bindForecastRepository(
        repo: AdapterForecastRepository
    ): IForecastRepository

}