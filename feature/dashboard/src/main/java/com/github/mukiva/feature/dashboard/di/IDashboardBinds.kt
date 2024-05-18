package com.github.mukiva.feature.dashboard.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IDashboardBinds {

//    @Binds
//    fun bindDashboardViewModel(
//        dashboardViewModel: DashboardViewModel,
//    ): IDashboardViewModel

//    @Binds
//    fun bindForecastViewModel(
//        forecastViewModel: ForecastStateHolder,
//    ): IForecastStateHolder
}
