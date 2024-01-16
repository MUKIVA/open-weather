package com.mukiva.feature.settings.di

import com.mukiva.feature.settings.presentation.SettingsState
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