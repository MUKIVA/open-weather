package com.mukiva.impl.di

import com.mukiva.api.navigation.ISettingsScreenProvider
import com.mukiva.api.repository.IGeneralSettingsSetter
import com.mukiva.api.repository.ISettingsRepository
import com.mukiva.impl.domain.repository.SettingsRepository
import com.mukiva.impl.navigation.SettingsScreenProvider
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