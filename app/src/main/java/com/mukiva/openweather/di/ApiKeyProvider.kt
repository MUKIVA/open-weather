package com.mukiva.openweather.di

import com.mukiva.core.network.IApiKeyProvider

data class ApiKeyProvider(
    override val apiKey: String
) : IApiKeyProvider
