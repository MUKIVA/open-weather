package com.mukiva.openweather.glue.settings.di

import com.mukiva.feature.settings.domain.repository.IForecastUpdater
import com.mukiva.feature.settings.domain.repository.IGeneralSettingsSetter
import com.mukiva.feature.settings.domain.repository.ISettingsRepository
import com.mukiva.openweather.glue.settings.repository.AdapterForecastUpdater
import com.mukiva.openweather.glue.settings.repository.AdapterSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IGlueSettingsBinds {

    @Binds
    fun bindSettingsRepository(
        adapterSettingsRepository: AdapterSettingsRepository
    ): ISettingsRepository

    @Binds
    fun bindSettingsSetter(
        adapterSettingsRepository: AdapterSettingsRepository
    ): IGeneralSettingsSetter

    @Binds
    fun bindForecastUpdater(
        updater: AdapterForecastUpdater
    ): IForecastUpdater

}