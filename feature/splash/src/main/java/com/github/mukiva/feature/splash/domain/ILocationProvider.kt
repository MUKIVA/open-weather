package com.github.mukiva.feature.splash.domain

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface ILocationProvider {
    fun getCurrentLocation(): Location?
}
