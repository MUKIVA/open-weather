package com.github.mukiva.openweather.glue.splash.di

import com.github.mukiva.feature.splash.navigation.ISplashRouter
import com.github.mukiva.openweather.glue.splash.navigation.SplashRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IGlueSplashBinds {

    @Binds
    fun bindSplashRouter(
        splashRouter: SplashRouter
    ): ISplashRouter
}
