package com.mukiva.core.data.repository.forecast.entity

import com.google.gson.annotations.SerializedName
import com.mukiva.core.data.entity.CurrentRemote
import com.mukiva.core.data.entity.LocationRemote

data class ForecastWithCurrentAndLocationRemote(
    @SerializedName("location")
    val location: LocationRemote? = null,
    @SerializedName("current")
    val current: CurrentRemote? = null,
    @SerializedName("forecast")
    val forecast: ForecastRemote? = null
)