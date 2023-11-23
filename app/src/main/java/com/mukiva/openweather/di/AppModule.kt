package com.mukiva.openweather.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.mukiva.core.navigation.INavigator
import com.mukiva.api.ConfigStore
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider
import com.mukiva.openweather.navigator.MainNavigator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideApiKeyProvider(apikey: String): IApiKeyProvider {
        return ApiKeyProvider(apikey)
    }
    @Provides
    fun provideConnectionProvider(application: Application): IConnectionProvider {
        return ConnectionProvider(application)
    }
    @[Singleton Provides]
    fun provideConfigStore(application: Application): ConfigStore {
        return ConfigStore(application)
    }
    @[Singleton Provides]
    fun provideNavigator(application: Application): INavigator {
        return ViewModelProvider.AndroidViewModelFactory(application)
            .create(MainNavigator::class.java)
    }
}