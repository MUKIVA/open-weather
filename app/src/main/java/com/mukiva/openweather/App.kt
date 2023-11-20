package com.mukiva.openweather

import android.app.Application
import com.di.DashboardDepsStore
import com.mukiva.feature.location_manager_impl.di.LocationManagerDepsStore
import com.mukiva.openweather.di.AppComponent
import com.mukiva.openweather.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .apiKey(BuildConfig.KEY_WEATHER_API)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        DashboardDepsStore.deps = appComponent
        LocationManagerDepsStore.deps = appComponent
    }
}