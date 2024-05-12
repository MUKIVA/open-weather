package com.mukiva.openweather.glue.dashboard.di

import com.mukiva.feature.dashboard.domain.repository.ISettingsRepository
import com.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.mukiva.openweather.glue.dashboard.navigation.DashboardRouter
import com.mukiva.openweather.glue.dashboard.repository.AdapterSettingsRepository
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

    @Binds
    fun bindSettingsMapper(
        repo: AdapterSettingsRepository
    ): ISettingsRepository

}