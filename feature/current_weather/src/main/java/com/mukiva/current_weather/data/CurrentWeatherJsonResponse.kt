package com.mukiva.current_weather.data

import com.google.gson.annotations.SerializedName


data class CurrentWeatherJsonResponse(
    @SerializedName("location")
    val location: LocationJson = LocationJson(),
    @SerializedName("current")
    val currentWeather: CurrentJson = CurrentJson()
)