package com.mukiva.core.data.repository.forecast.di

import com.mukiva.core.data.repository.forecast.gateway.IForecastGateway
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ForecastRepositoryModule {
    @Provides
    fun provideForecastGateway(
        retrofit: Retrofit
    ): IForecastGateway = retrofit.create(IForecastGateway::class.java)

}