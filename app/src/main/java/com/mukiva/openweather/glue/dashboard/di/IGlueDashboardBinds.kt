package com.mukiva.openweather.glue.dashboard.di

import com.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.mukiva.openweather.glue.dashboard.navigation.DashboardRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface IGlueDashboardBinds {
    @Binds
    fun bindDashboardRouter(
        router: DashboardRouter
    ): IDashboardRouter

}