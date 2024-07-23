package com.github.mukiva.feature.splash.domain

import android.location.Location

interface ILocationProvider {
    fun getCurrentLocation(): Location?
}
