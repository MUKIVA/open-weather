package com.mukiva.openweather.glue.dashboard.di

import com.mukiva.feature.dashboard.ui.IMinimalForecastFragmentFactory
import com.mukiva.openweather.glue.dashboard.ui.MinimalForecastFragmentFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IGlueDashboardUiModule {

    @Binds
    fun bindMinimalForecastFragmentFactory(
        factory: MinimalForecastFragmentFactory
    ): IMinimalForecastFragmentFactory

}