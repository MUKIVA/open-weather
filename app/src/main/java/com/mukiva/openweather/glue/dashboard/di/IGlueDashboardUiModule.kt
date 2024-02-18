package com.mukiva.openweather.glue.dashboard.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IGlueDashboardUiModule {

//    @Binds
//    fun bindMinimalForecastFragmentFactory(
//        factory: MinimalForecastFragmentFactory
//    ): IMinimalForecastFragmentFactory

}