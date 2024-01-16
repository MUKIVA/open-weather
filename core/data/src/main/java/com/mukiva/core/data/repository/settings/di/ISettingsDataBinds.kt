package com.mukiva.core.data.repository.settings.di

import com.mukiva.core.data.repository.settings.ISettingsRepository
import com.mukiva.core.data.repository.settings.impl.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ISettingsDataBinds {

    @Binds
    @Singleton
    fun bindSettingsRepository(
        repo: SettingsRepositoryImpl
    ): ISettingsRepository


}