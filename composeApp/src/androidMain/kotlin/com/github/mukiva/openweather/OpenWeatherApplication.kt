package com.github.mukiva.openweather

import android.app.Application
import com.github.mukiva.openweather.di.androidGlobalModule
import com.github.mukiva.openweather.di.commonGlobalModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class OpenWeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@OpenWeatherApplication)
            modules(commonGlobalModule + androidGlobalModule)
        }

    }

}