package com.mukiva.openweather.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModelProvider
import com.di.DashboardDependencies
import com.mukiva.core.navigation.INavigator
import com.mukiva.feature.location_manager_impl.di.LocationManagerDependencies
import com.mukiva.openweather.core.di.IApiKeyProvider
import com.mukiva.openweather.core.di.IConnectionProvider
import com.mukiva.openweather.navigator.MainNavigator
import com.mukiva.openweather.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope
import javax.inject.Singleton

@[AppScope Singleton Component(modules = [AppModule::class])]
interface AppComponent :
    DashboardDependencies,
    LocationManagerDependencies {

    val apiKey: String

    fun inject(activity: MainActivity)

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

//    private val mNavigator: INavigator by lazy {
//        ViewModelProvider.AndroidViewModelFactory(application)
//            .create(MainNavigator::class.java)
//    }

    @Provides
    fun provideApiKeyProvider(apikey: String): IApiKeyProvider {
        return ApiKeyProvider(apikey)
    }

    @Provides
    fun provideConnectionProvider(application: Application): IConnectionProvider {
        return ConnectionProvider(application)
    }
    @[Singleton Provides]
    fun provideNavigator(application: Application): INavigator {
        return ViewModelProvider.AndroidViewModelFactory(application)
            .create(MainNavigator::class.java)
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
