package com.github.mukiva.openweather.glue.dashboard.di

import com.github.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.github.mukiva.openweather.glue.dashboard.navigation.DashboardRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface IGlueDashboardBinds {
    @Binds
    fun bindDashboardRouter(
        router: DashboardRouter
    ): IDashboardRouter
}
