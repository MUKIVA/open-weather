package com.mukiva.openweather.di

import com.mukiva.openweather.core.di.IApiKeyProvider

data class ApiKeyProvider(override val apiKey: String) : IApiKeyProvider
