package com.github.mukiva.openweather.glue.splash.di

import com.github.mukiva.feature.splash.navigation.ISplashRouter
import com.github.mukiva.openweather.glue.splash.navigation.SplashRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface IGlueSplashBinds {
    @Binds
    fun bindSplashNavigator(
        splashRouter: SplashRouter
    ): ISplashRouter
}
