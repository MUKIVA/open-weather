package com.domain.gateway

import com.data.CurrentWeatherJsonResponse
import com.mukiva.openweather.data.IGateway
import retrofit2.http.GET
import retrofit2.http.Query

interface ICurrentWeatherGateway : IGateway {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("aqi") aqi: String
    ) : CurrentWeatherJsonResponse
}