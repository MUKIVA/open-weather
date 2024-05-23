package com.github.mukiva.openweather.glue.navigation.di

import com.github.mukiva.navigation.router.GlobalRouter
import com.github.mukiva.navigation.router.INavigationResourcesProvider
import com.github.mukiva.navigation.ui.ILifecycleHandler
import com.github.mukiva.navigation.ui.IWeatherNotificationServiceLauncher
import com.github.mukiva.navigation.ui.SettingsHandler
import com.github.mukiva.openweather.glue.navigation.DefaultNavigationResourcesProvider
import com.github.mukiva.openweather.service.WeatherNotificationServiceLauncher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet

@Module
@InstallIn(SingletonComponent::class)
class GlueNavigationModule {

    @Provides
    fun provideNavigationResourcesProvider(): INavigationResourcesProvider =
        DefaultNavigationResourcesProvider()

    @Provides
    @ElementsIntoSet
    fun provideLifecycleHandlerSet(
        globalRouter: GlobalRouter,
        settingsHandler: SettingsHandler
    ): Set<@JvmSuppressWildcards ILifecycleHandler> {
        val set = hashSetOf<ILifecycleHandler>()
        set.add(globalRouter)
        set.add(settingsHandler)
        return set
    }

    @Provides
    fun provideWeatherNotificationServiceLauncher(
        weatherNotificationServiceLauncher: WeatherNotificationServiceLauncher
    ): IWeatherNotificationServiceLauncher = object : IWeatherNotificationServiceLauncher {
        override fun startService() {
            weatherNotificationServiceLauncher.startService()
        }

        override fun stopService() {
            weatherNotificationServiceLauncher.stopService()
        }
    }
}
