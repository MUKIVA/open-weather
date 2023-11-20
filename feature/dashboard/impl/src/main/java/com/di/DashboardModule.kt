package com.di

import com.domain.gateway.ICurrentWeatherGateway
import com.mukiva.openweather.core.di.FeatureScope
import com.presentation.DashboardState
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DashboardModule {

    @[Provides FeatureScope]
    fun provideCurrentWeatherState() = DashboardState.default()

    @[Provides FeatureScope]
    fun provideCurrentWeatherGateway(): ICurrentWeatherGateway {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ICurrentWeatherGateway::class.java)
    }

}