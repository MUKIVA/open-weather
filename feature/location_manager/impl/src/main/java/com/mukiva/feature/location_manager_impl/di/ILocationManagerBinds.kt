package com.mukiva.feature.location_manager_impl.di

import com.mukiva.feature.location_manager_api.navigation.ILocationManagerScreenProvider
import com.mukiva.feature.location_manager_api.repository.ILocationRepository
import com.mukiva.feature.location_manager_impl.data.LocationRepository
import com.mukiva.feature.location_manager_impl.navigation.LocationManagerScreenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ILocationManagerBinds {

    @Binds
    fun bindLocationRepository(repo: LocationRepository): ILocationRepository

    @Binds
    fun bindLocationScreenProvider(
        provider: LocationManagerScreenProvider
    ): ILocationManagerScreenProvider

}