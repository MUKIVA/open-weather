package com.github.mukiva.openweather.glue.splash.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import com.github.mukiva.feature.splash.domain.ILocationProvider

internal class LocationProvider internal constructor(
    private val applicationContext: Context
) : ILocationProvider {

    private val mLocationManager by lazy {
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    override fun getCurrentLocation(): Location? {
        val coarsePermissionCheck = applicationContext
            .checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        val finePermissionCheck = applicationContext
            .checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (coarsePermissionCheck != PackageManager.PERMISSION_GRANTED &&
            finePermissionCheck != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }
        return mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }
}
