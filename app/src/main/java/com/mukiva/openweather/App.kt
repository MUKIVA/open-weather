package com.mukiva.openweather

import android.app.Application
import com.mukiva.current_weather.di.CurrentWeatherDepsStore
import com.mukiva.location_search.di.LocationSearchDepsStore
import com.mukiva.openweather.di.AppComponent
import com.mukiva.openweather.di.DaggerAppComponent

class App : Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .apiKey(BuildConfig.KEY_WEATHER_API)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        CurrentWeatherDepsStore.deps = appComponent
        LocationSearchDepsStore.deps = appComponent
    }
}