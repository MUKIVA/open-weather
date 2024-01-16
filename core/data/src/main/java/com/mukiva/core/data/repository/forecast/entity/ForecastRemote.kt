package com.mukiva.core.data.repository.forecast.entity

import com.google.gson.annotations.SerializedName

data class ForecastRemote(
    @SerializedName("forecastday")
    val forecastDay: List<ForecastDayRemote> = emptyList()
)