package com.di

import com.domain.gateway.ICurrentWeatherGateway
import com.presentation.DashboardState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class DashboardModule {

    @Provides
    fun provideDashboardState() = DashboardState.default()

    @Provides
    fun provideCurrentWeatherGateway(
        retrofit: Retrofit
    ): ICurrentWeatherGateway {
        return retrofit.create(ICurrentWeatherGateway::class.java)
    }

}