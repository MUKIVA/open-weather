package com.data

import com.google.gson.annotations.SerializedName

data class CurrentWeatherRemote(
    @SerializedName("location")
    val location: LocationRemote = LocationRemote(),
    @SerializedName("current")
    val currentWeather: CurrentRemote = CurrentRemote()
)