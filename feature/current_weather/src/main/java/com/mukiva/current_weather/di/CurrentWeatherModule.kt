package com.mukiva.current_weather.di

import android.content.res.Configuration
import android.graphics.Bitmap.Config
import com.mukiva.current_weather.domain.gateway.ICurrentWeatherGateway
import com.mukiva.current_weather.presentation.CurrentWeatherState
import com.mukiva.openweather.core.di.FeatureScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
class CurrentWeatherModule {

    @[Provides FeatureScope]
    fun provideCurrentWeatherState() = CurrentWeatherState.default()

    @[Provides FeatureScope]
    fun provideCurrentWeatherGateway(): ICurrentWeatherGateway {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ICurrentWeatherGateway::class.java)
    }

}