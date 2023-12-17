package com.domain.gateway

import com.data.CurrentWeatherRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface ICurrentWeatherGateway {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("aqi") aqi: String
    ) : CurrentWeatherRemote
}