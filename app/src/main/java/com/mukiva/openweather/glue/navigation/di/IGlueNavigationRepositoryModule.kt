package com.mukiva.openweather.glue.navigation.di

import com.mukiva.navigation.domain.repository.ISettingsRepository
import com.mukiva.openweather.glue.navigation.repository.AdapterSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IGlueNavigationRepositoryModule {

    @Binds
    fun bindSettingsRepository(
        adapterSettingsRepository: AdapterSettingsRepository
    ): ISettingsRepository
}