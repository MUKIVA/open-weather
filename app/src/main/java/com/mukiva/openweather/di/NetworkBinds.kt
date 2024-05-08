package com.mukiva.openweather.di

import com.mukiva.core.network.IApiKeyProvider
import com.mukiva.core.network.IConnectionProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkBinds {

    @Binds
    @Singleton
    fun bindNetworkStateProvider(connectionProvider: ConnectionProvider): IConnectionProvider

//    @Binds
//    @Singleton
//    fun bindApiKeyProvider(apiKeyProvider: ApiKeyProvider): IApiKeyProvider

}