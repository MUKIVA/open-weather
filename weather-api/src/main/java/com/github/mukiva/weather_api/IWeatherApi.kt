package com.github.mukiva.weather_api

import com.github.mukiva.weather_api.models.ForecastWithCurrentAndLocationDto
import com.github.mukiva.weather_api.models.LocationDto
import com.github.mukiva.weather_api.utils.WeatherApiKeyInterceptor
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherApi {
    @GET("/forecast.json")
    suspend fun forecast(
        @Query(value = "q") q: String,
        @Query(value = "days") days: Int,
    ): Result<ForecastWithCurrentAndLocationDto>

    @GET("/search.json")
    suspend fun search(
        @Query(value = "q") q: String,
    ): Result<List<LocationDto>>

}

fun weatherApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json
) = retrofit(baseUrl, apiKey, okHttpClient, json)

private fun retrofit(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json
): Retrofit {
    val converterFactory = json.asConverterFactory(
        contentType = "application/json; charset=UTF8".toMediaType()
    )

    val modifiedClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .addInterceptor(WeatherApiKeyInterceptor(apiKey))
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(modifiedClient)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .addConverterFactory(converterFactory)
        .build()
}