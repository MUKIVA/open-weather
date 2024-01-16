package com.mukiva.openweather.glue.settings.di

import com.mukiva.feature.settings.domain.repository.IGeneralSettingsSetter
import com.mukiva.feature.settings.domain.repository.ISettingsRepository
import com.mukiva.openweather.glue.settings.repository.AdapterSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IGlueSettingsRepositoryModule {

    @Binds
    fun bindSettingsRepository(
        adapterSettingsRepository: AdapterSettingsRepository
    ): ISettingsRepository

    @Binds
    fun bindSettingsSetter(
        adapterSettingsRepository: AdapterSettingsRepository
    ): IGeneralSettingsSetter

}