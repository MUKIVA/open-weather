package com.mukiva.feature.dashboard_impl.data

import com.google.gson.annotations.SerializedName
import com.mukiva.core.data.CurrentRemote
import com.mukiva.core.data.LocationRemote

data class CurrentWeatherRemote(
    @SerializedName("location")
    val location: LocationRemote,
    @SerializedName("current")
    val currentWeather: CurrentRemote
)