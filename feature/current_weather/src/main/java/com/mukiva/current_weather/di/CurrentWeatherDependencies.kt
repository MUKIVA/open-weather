package com.mukiva.current_weather.di

import com.mukiva.core.navigation.INavigator
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider

interface CurrentWeatherDependencies {
    val apiKeyProvider: IApiKeyProvider
    val connectionProvider: IConnectionProvider
    val navigator: INavigator
}