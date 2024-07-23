package com.github.mukiva.feature.dashboard.di

import android.content.Context
import com.github.mukiva.feature.dashboard.presentation.ForecastLoader
import com.github.mukiva.feature.dashboard.presentation.IForecastLoader
import com.github.mukiva.feature.dashboard.ui.widget.CurrentWeatherWidgetProvider
import dagger.Binds
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

@Module
@InstallIn(SingletonComponent::class)
interface DashboardBinds {

    @Binds
    fun bindForecastLoader(
        forecastLoader: ForecastLoader
    ): IForecastLoader

}
