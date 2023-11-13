package com.mukiva.openweather.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.mukiva.current_weather.di.CurrentWeatherDependencies
import com.mukiva.location_search.di.LocationSearchDependencies
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@[AppScope Component(modules = [AppModule::class])]
interface AppComponent :
    CurrentWeatherDependencies,
    LocationSearchDependencies {

    val apiKey: String

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        @BindsInstance
        fun apiKey(key: String): Builder
        fun build(): AppComponent
    }

}

@Scope
annotation class AppScope

@Module
class AppModule {
    @Provides
    fun provideApiKeyProvider(apikey: String): IApiKeyProvider {
        return ApiKeyProvider(apikey)
    }

    @Provides
    fun provideConnectionProvider(application: Application): IConnectionProvider {
        return ConnectionProvider(application)
    }
}

data class ApiKeyProvider(override val apiKey: String) : IApiKeyProvider

class ConnectionProvider(private val context: Context) : IConnectionProvider {
    override fun hasConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val an = cm.activeNetwork ?: return false
        val actNw = cm.getNetworkCapabilities(an) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

}
