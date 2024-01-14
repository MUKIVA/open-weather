package com.mukiva.feature.forecast_impl.data

import com.google.gson.annotations.SerializedName
import com.mukiva.core.data.CurrentRemote
import com.mukiva.core.data.LocationRemote

data class ForecastWithCurrentAndLocationRemote(
    @SerializedName("location")
    val locations: LocationRemote? = null,
    @SerializedName("current")
    val current: CurrentRemote? = null,
    @SerializedName("forecast")
    val forecast: ForecastRemote? = null
)