package com.mukiva.openweather.glue.dashboard.di

import com.mukiva.feature.dashboard.domain.repository.ICurrentWeatherRepository
import com.mukiva.feature.dashboard.domain.repository.ILocationRepository
import com.mukiva.feature.dashboard.domain.repository.ISettingsRepository
import com.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.mukiva.openweather.glue.dashboard.navigation.DashboardRouter
import com.mukiva.openweather.glue.dashboard.repository.AdapterCurrentWeatherRepository
import com.mukiva.openweather.glue.dashboard.repository.AdapterLocationRepository
import com.mukiva.openweather.glue.dashboard.repository.AdapterSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface IGlueDashboardRepositoryModule {
    @Binds
    fun bindDashboardRouter(
        router: DashboardRouter
    ): IDashboardRouter
    @Binds
    fun bindCurrentWeatherRepository(
        repo: AdapterCurrentWeatherRepository
    ): ICurrentWeatherRepository
    @Binds
    fun bindLocationRepository(
        repo: AdapterLocationRepository
    ): ILocationRepository
    @Binds
    fun bindSettingsMapper(
        repo: AdapterSettingsRepository
    ): ISettingsRepository
}