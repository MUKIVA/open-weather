package com.mukiva.core.data.repository.location.di

import com.mukiva.core.data.repository.location.ILocationRepository
import com.mukiva.core.data.repository.location.impl.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ILocationManagerBinds {

    @Binds
    @Singleton
    fun bindsLocationRepository(
        repo: LocationRepositoryImpl
    ): ILocationRepository

}