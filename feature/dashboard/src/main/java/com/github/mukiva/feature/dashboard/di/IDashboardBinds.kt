package com.github.mukiva.feature.dashboard.di

import com.github.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.github.mukiva.feature.dashboard.presentation.ForecastStateHolder
import com.github.mukiva.feature.dashboard.presentation.IDashboardViewModel
import com.github.mukiva.feature.dashboard.presentation.IForecastStateHolder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IDashboardBinds {

    @Binds
    fun bindDashboardViewModel(
        dashboardViewModel: DashboardViewModel,
    ): IDashboardViewModel

//    @Binds
//    fun bindForecastViewModel(
//        forecastViewModel: ForecastStateHolder,
//    ): IForecastStateHolder
}
