package com.mukiva.core.data.repository.forecast.gateway

import com.mukiva.core.data.repository.forecast.entity.ForecastWithCurrentAndLocationRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface IForecastGateway {
    @GET("forecast.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int = 0,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ) : ForecastWithCurrentAndLocationRemote
}