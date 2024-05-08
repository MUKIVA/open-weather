package com.mukiva.openweather.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.mukiva.core.network.IConnectionProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ConnectionProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : IConnectionProvider {
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