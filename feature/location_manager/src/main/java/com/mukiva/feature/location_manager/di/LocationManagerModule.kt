package com.mukiva.feature.location_manager.di

import com.mukiva.feature.location_manager.presentation.LocationManagerState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LocationManagerModule {

    @Provides
    fun provideLocationManagerState() = LocationManagerState.default()

}