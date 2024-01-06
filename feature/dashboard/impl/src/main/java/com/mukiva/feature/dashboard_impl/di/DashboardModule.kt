package com.mukiva.feature.dashboard_impl.di

import com.mukiva.feature.dashboard_impl.domain.gateway.ICurrentWeatherGateway
import com.mukiva.feature.dashboard_impl.presentation.AdditionalInfoState
import com.mukiva.feature.dashboard_impl.presentation.DashboardState
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
    fun provideAdditionalInfoState() = AdditionalInfoState.default()

    @Provides
    fun provideCurrentWeatherGateway(
        retrofit: Retrofit
    ): ICurrentWeatherGateway {
        return retrofit.create(ICurrentWeatherGateway::class.java)
    }

}