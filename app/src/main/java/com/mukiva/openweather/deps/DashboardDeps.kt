package com.mukiva.openweather.deps

import com.di.IDashboardDependencies
import com.mukiva.api.navigation.ISettingsScreenProvider
import com.mukiva.core.navigation.INavigator
import com.mukiva.feature.location_manager_api.navigation.ILocationManagerScreenProvider
import com.mukiva.feature.location_manager_impl.navigation.LocationManagerScreenProvider
import com.mukiva.impl.navigation.SettingsScreenProvider
import com.mukiva.openweather.App
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider
import com.mukiva.openweather.di.ApiKeyProvider
import com.mukiva.openweather.di.ConnectionProvider

object DashboardDeps : IDashboardDependencies {
    override val apiKeyProvider: IApiKeyProvider
        get() = ApiKeyProvider(App.component.config.apiKey)
    override val connectionProvider: IConnectionProvider
        get() = ConnectionProvider(App.component.application)
    override val navigator: INavigator
        get() = App.component.navigator
    override val settingsScreenProvider: ISettingsScreenProvider
        get() = SettingsScreenProvider()
    override val locationManagerScreenProvider: ILocationManagerScreenProvider
        get() = LocationManagerScreenProvider()
}