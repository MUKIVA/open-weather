package com.github.mukiva.weather_api.utils

import okhttp3.Interceptor
import okhttp3.Response

class WeatherApiKeyInterceptor(
    private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val newRequestBuilder = originalRequest.newBuilder()
        val newUrlBuildr = originalUrl.newBuilder()
        val newUrl = newUrlBuildr
            .addQueryParameter("key", apiKey)
            .build()

        val newRequest = newRequestBuilder
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}