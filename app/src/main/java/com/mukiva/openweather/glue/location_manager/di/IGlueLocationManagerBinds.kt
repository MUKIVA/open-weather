package com.mukiva.openweather.glue.location_manager.di

import com.mukiva.feature.location_manager.domain.repository.IForecastUpdater
import com.mukiva.feature.location_manager.domain.repository.ILocationRepository
import com.mukiva.feature.location_manager.navigation.ILocationManagerRouter
import com.mukiva.openweather.glue.location_manager.navigation.LocationManagerRouter
import com.mukiva.openweather.glue.location_manager.repository.AdapterForecastUpdater
import com.mukiva.openweather.glue.location_manager.repository.AdapterLocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface IGlueLocationManagerBinds {

    @Binds
    fun bindLocationManagerRouter(
        locationManagerRouter: LocationManagerRouter
    ): ILocationManagerRouter

    @Binds
    fun bindLocationRepository(
        repository: AdapterLocationRepository
    ): ILocationRepository

    @Binds
    fun bindForecastUpdater(
        updater: AdapterForecastUpdater
    ): IForecastUpdater

}