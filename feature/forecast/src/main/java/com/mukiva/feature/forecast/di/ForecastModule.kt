package com.mukiva.feature.forecast.di

import com.mukiva.feature.forecast.presentation.ForecastState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class ForecastModule {
    @Provides
    fun provideForecastState() = ForecastState.default()
//    @Provides
//    fun provideMinimalForecastState() = MinimalForecastState.default()
}