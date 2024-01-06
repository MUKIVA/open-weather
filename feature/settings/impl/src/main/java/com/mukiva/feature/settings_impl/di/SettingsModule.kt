package com.mukiva.feature.settings_impl.di

import com.mukiva.feature.settings_impl.presentation.SettingsState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {
    @Provides
    fun provideSettingsState() = SettingsState.default()

}