package com.mukiva.feature.dashboard.di

import com.mukiva.feature.dashboard.presentation.AdditionalInfoState
import com.mukiva.feature.dashboard.presentation.DashboardState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DashboardModule {

    @Provides
    fun provideDashboardState() = DashboardState.default()

    @Provides
    fun provideAdditionalInfoState() = AdditionalInfoState.default()

}