package com.mukiva.openweather.deps

import android.app.Application
import com.mukiva.feature.location_manager_impl.di.ILocationManagerDependencies
import com.mukiva.openweather.App
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider
import com.mukiva.openweather.di.ApiKeyProvider
import com.mukiva.openweather.di.ConnectionProvider

object LocationManagerDeps : ILocationManagerDependencies {
    override val apiKeyProvider: IApiKeyProvider
        get() = ApiKeyProvider(App.component.config.apiKey)
    override val connectionProvider: IConnectionProvider
        get() = ConnectionProvider(App.component.application)
    override val application: Application
        get() = App.component.application
}