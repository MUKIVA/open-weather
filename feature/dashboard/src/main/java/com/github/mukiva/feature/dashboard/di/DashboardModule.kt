package com.github.mukiva.feature.dashboard.di

import android.content.Context
import com.github.mukiva.feature.dashboard.ui.widget.CurrentWeatherWidgetProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DashboardModule {
    @Provides
    fun provideCurrentWeatherUpdater(
        @ApplicationContext applicationContext: Context
    ) = CurrentWeatherWidgetProvider.Updater(applicationContext)
}
