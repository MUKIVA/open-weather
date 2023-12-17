package com.mukiva.openweather.di

import com.mukiva.openweather.BuildConfig
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkBinds {

    @Binds
    @Singleton
    fun bindNetworkStateProvider(connectionProvider: ConnectionProvider): IConnectionProvider

    @Binds
    @Singleton
    fun bindApiKeyProvider(apiKeyProvider: ApiKeyProvider): IApiKeyProvider

}

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiKey(): ApiKeyProvider {
        return ApiKeyProvider(
            BuildConfig.KEY_WEATHER_API
        )
    }

}