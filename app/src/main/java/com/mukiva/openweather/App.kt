package com.mukiva.openweather

import android.app.Application
import com.mukiva.openweather.di.AppConfig
import com.mukiva.openweather.di.ComponentHelper
import com.mukiva.openweather.di.DaggerIAppComponent
import com.mukiva.openweather.di.IAppComponent

class App : Application() {

    init {
        mInstance = this
    }

    val appComponent: IAppComponent by lazy {
        DaggerIAppComponent.builder()
            .application(this)
            .config(AppConfig(
                apiKey = BuildConfig.KEY_WEATHER_API,
                baseUrl = BuildConfig.WEATHER_API_BASE_URL
            ))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        ComponentHelper.initAll()
    }

    companion object {

        private var mInstance: App? = null
        val component: IAppComponent
            get() = mInstance!!.appComponent
    }
}