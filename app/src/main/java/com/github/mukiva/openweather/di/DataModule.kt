package com.github.mukiva.openweather.di

import android.content.Context
import com.github.mukiva.feature.weathernotification.WeatherNotificationWorker
import com.github.mukiva.feature.weathernotification.WeatherNotificationWorkerFactory
import com.github.mukiva.openweather.BuildConfig
import com.github.mukiva.openweather.createMainWorkerFactory
import com.github.mukiva.weatherapi.IWeatherApi
import com.github.mukiva.weatherapi.weatherApi
import com.github.mukiva.weatherdata.IForecastRepository
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.createForecastRepository
import com.github.mukiva.weatherdata.createLocationRepository
import com.github.mukiva.weatherdata.createSettingsRepository
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
internal class DataModule {

    @Provides
    @Singleton
    fun provideMainWorkerFactory(
        weatherNotificationWorkerFactory: WeatherNotificationWorkerFactory
    ) = createMainWorkerFactory(weatherNotificationWorkerFactory)

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
    @Singleton
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
    @Singleton
    fun provideWeatherApiGateway(
        retrofit: Retrofit
    ): IWeatherApi = retrofit.create(IWeatherApi::class.java)

    @Provides
    @Singleton
    fun provideWeatherDatabase(
        @ApplicationContext applicationContext: Context
    ): WeatherDatabase = weatherDatabase(applicationContext)

    @Provides
    @Singleton
    fun provideLocationRepository(
        database: WeatherDatabase,
        api: IWeatherApi,
    ): ILocationRepository = createLocationRepository(database, api)

    @Provides
    @Singleton
    fun provideForecastRepository(
        database: WeatherDatabase,
        api: IWeatherApi,
    ): IForecastRepository = createForecastRepository(database, api)

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext applicationContext: Context
    ): ISettingsRepository = createSettingsRepository(applicationContext)
}
