package com.github.mukiva.openweather.di

import android.content.Context
import com.github.mukiva.openweather.BuildConfig
import com.github.mukiva.weatherapi.IWeatherApi
import com.github.mukiva.weatherapi.weatherApi
import com.github.mukiva.weatherdata.ForecastRepository
import com.github.mukiva.weatherdata.LocationRepository
import com.github.mukiva.weatherdata.SettingsRepository
import com.github.mukiva.weatherdatabase.WeatherDatabase
import com.github.mukiva.weatherdatabase.weatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        json: Json,
    ): Retrofit = weatherApi(
        baseUrl = BuildConfig.WEATHER_API_BASE_URL,
        apiKey = BuildConfig.KEY_WEATHER_API,
        okHttpClient = client,
        json = json,
    )

    @Provides
    fun provideWeatherApiGateway(
        retrofit: Retrofit
    ): IWeatherApi = retrofit.create(IWeatherApi::class.java)

    @Provides
    fun provideWeatherDatabase(
        @ApplicationContext applicationContext: Context
    ): WeatherDatabase = weatherDatabase(applicationContext)

    @Provides
    fun provideLocationRepository(
        database: WeatherDatabase,
        api: IWeatherApi,
    ): LocationRepository = LocationRepository(database, api)

    @Provides
    fun provideForecastRepository(
        database: WeatherDatabase,
        api: IWeatherApi,
    ): ForecastRepository = ForecastRepository(database, api)

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext applicationContext: Context
    ): SettingsRepository = SettingsRepository(applicationContext)
}
