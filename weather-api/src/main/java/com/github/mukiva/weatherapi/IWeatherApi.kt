package com.github.mukiva.weatherapi

import com.github.mukiva.weatherapi.models.ForecastWithCurrentAndLocationDto
import com.github.mukiva.weatherapi.models.LocationDto
import com.github.mukiva.weatherapi.utils.WeatherApiKeyInterceptor
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

public interface IWeatherApi {
    @GET("forecast.json")
    public suspend fun forecast(
        @Query(value = "q") q: String,
        @Query(value = "days") days: Int,
        @Query(value = "lang") lang: String
    ): Result<ForecastWithCurrentAndLocationDto>

    @GET("search.json")
    public suspend fun search(
        @Query(value = "q") q: String,
        @Query(value = "lang") lang: String
    ): Result<List<LocationDto>>
}

public fun weatherApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json
): Retrofit = retrofit(baseUrl, apiKey, okHttpClient, json)

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
