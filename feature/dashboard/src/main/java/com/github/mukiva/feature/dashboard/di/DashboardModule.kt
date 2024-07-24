package com.github.mukiva.feature.dashboard.di

import com.github.mukiva.feature.dashboard.presentation.ForecastLoader
import com.github.mukiva.feature.dashboard.presentation.IForecastLoader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DashboardBinds {

    @Binds
    fun bindForecastLoader(
        forecastLoader: ForecastLoader
    ): IForecastLoader

}
