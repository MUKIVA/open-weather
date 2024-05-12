package com.mukiva.openweather.glue.forecast.di

import com.mukiva.feature.forecast.domain.repository.ISettingsRepository
import com.mukiva.feature.forecast.navigation.IForecastRouter
import com.mukiva.openweather.glue.forecast.navigation.ForecastRouter
import com.mukiva.openweather.glue.forecast.repository.AdapterSettingsRepository
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
    fun bindSettingsRepository(
        repo: AdapterSettingsRepository
    ): ISettingsRepository

}