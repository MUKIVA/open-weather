package com.mukiva.openweather.glue.forecast.di

import com.mukiva.feature.forecast.navigation.IForecastRouter
import com.mukiva.openweather.glue.forecast.navigation.ForecastRouter
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
}