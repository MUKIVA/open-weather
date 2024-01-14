package com.mukiva.feature.forecast_impl.data

import com.google.gson.annotations.SerializedName

data class ForecastRemote(
    @SerializedName("forecastday")
    val forecastDay: List<ForecastDayRemote> = emptyList()
)