package com.di

import com.mukiva.api.navigation.ISettingsScreenProvider
import com.mukiva.core.navigation.INavigator
import com.mukiva.feature.location_manager_api.navigation.ILocationManagerScreenProvider
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider

interface IDashboardDependencies {
    val apiKeyProvider: IApiKeyProvider
    val connectionProvider: IConnectionProvider
    val navigator: INavigator

    val settingsScreenProvider: ISettingsScreenProvider
    val locationManagerScreenProvider: ILocationManagerScreenProvider
}