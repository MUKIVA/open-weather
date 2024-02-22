package com.mukiva.core.data.repository.forecast.gateway

import com.mukiva.core.data.repository.forecast.entity.ForecastWithCurrentAndLocationRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface IForecastGateway {
    @GET("forecast.json")
    @JvmSuppressWildcards
    suspend fun getCurrentWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int = DEFAULT_DAYS_COUNT,
        @Query("aqi") aqi: String = DEFAULT_AQI,
        @Query("alerts") alerts: String = DEFAULT_ALERTS
    ) : ForecastWithCurrentAndLocationRemote

    companion object {
        private const val DEFAULT_DAYS_COUNT = 3
        private const val DEFAULT_AQI = "no"
        private const val DEFAULT_ALERTS = "no"
    }
}