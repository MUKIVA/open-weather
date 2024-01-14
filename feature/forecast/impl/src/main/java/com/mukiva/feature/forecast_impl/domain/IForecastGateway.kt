package com.mukiva.feature.forecast_impl.domain

import com.mukiva.feature.forecast_impl.data.ForecastWithCurrentAndLocationRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface IForecastGateway {
    @GET("forecast.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String
    ) : ForecastWithCurrentAndLocationRemote
}