package com.di

import com.mukiva.core.navigation.INavigator
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider

interface DashboardDependencies {
    val apiKeyProvider: IApiKeyProvider
    val connectionProvider: IConnectionProvider
    val navigator: INavigator
}