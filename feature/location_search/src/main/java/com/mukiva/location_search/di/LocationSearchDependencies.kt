package com.mukiva.location_search.di

import android.app.Application
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider

interface LocationSearchDependencies {
    val apiKeyProvider: IApiKeyProvider
    val connectionProvider: IConnectionProvider
    val application: Application
}