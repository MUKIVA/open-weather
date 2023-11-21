package com.mukiva.impl.di

import com.mukiva.impl.presentation.SettingsState
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    fun provideSettingsState() = SettingsState.default()

}