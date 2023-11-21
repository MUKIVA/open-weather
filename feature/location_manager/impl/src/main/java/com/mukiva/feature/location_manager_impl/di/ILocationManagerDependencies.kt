package com.mukiva.feature.location_manager_impl.di

import android.app.Application
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider

interface ILocationManagerDependencies {
    val apiKeyProvider: IApiKeyProvider
    val connectionProvider: IConnectionProvider
    val application: Application
}