package com.github.mukiva.openweather.glue.splash.di

import android.content.Context
import com.github.mukiva.feature.splash.domain.ILocationProvider
import com.github.mukiva.openweather.glue.splash.utils.LocationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GlueSplashUtilsModule {

    @Provides
    fun provideLocationProvider(
        @ApplicationContext applicationContext: Context
    ): ILocationProvider = LocationProvider(applicationContext)
}
