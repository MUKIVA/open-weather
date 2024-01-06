package com.mukiva.feature.settings_impl.di

import com.mukiva.feature.settings_api.navigation.ISettingsScreenProvider
import com.mukiva.feature.settings_api.repository.IGeneralSettingsSetter
import com.mukiva.feature.settings_api.repository.ISettingsRepository
import com.mukiva.feature.settings_impl.domain.repository.SettingsRepository
import com.mukiva.feature.settings_impl.navigation.SettingsScreenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ISettingsBinds {
    @Binds
    fun bindGeneralSettingsSetter(setter: SettingsRepository): IGeneralSettingsSetter

    @Binds
    fun bindSettingsRepository(repo: SettingsRepository): ISettingsRepository

    @Binds
    fun bindSettingsScreenProvider(provider: SettingsScreenProvider): ISettingsScreenProvider

}