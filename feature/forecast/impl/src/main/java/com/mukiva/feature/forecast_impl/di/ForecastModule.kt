package com.mukiva.feature.forecast_impl.di

import com.mukiva.feature.forecast_api.IForecastFragmentProvider
import com.mukiva.feature.forecast_impl.ForecastFragmentProvider
import com.mukiva.feature.forecast_impl.domain.IForecastGateway
import com.mukiva.feature.forecast_impl.presentation.ForecastState
import com.mukiva.feature.forecast_impl.presentation.MinimalForecastState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ForecastModule {
    @Provides
    fun provideForecastState() = ForecastState.default()
    @Provides
    fun provideMinimalForecastState() = MinimalForecastState.default()

    @Provides
    fun provideForecastGateway(
        retrofit: Retrofit
    ): IForecastGateway = retrofit.create(IForecastGateway::class.java)

    @Provides
    fun provideFragmentProvider(): IForecastFragmentProvider = ForecastFragmentProvider()

}