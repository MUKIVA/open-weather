package com.github.mukiva.openweather.glue.locationmanager.di

import com.github.mukiva.feature.locationmanager.navigation.ILocationManagerRouter
import com.github.mukiva.openweather.glue.locationmanager.navigation.LocationManagerRouter
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
}
