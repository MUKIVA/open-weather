package com.mukiva.feature.forecast_impl.data

import com.google.gson.annotations.SerializedName
import com.mukiva.core.data.AstroRemote
import com.mukiva.core.data.DayRemote
import java.util.Date

data class ForecastDayRemote(
    @SerializedName("date")
    val date: Date? = null,
    @SerializedName("date_epoch")
    val dateEpoch: Int? = null,
    @SerializedName("day")
    val day: DayRemote? = null,
    @SerializedName("astro")
    val astro: AstroRemote? = null,
    @SerializedName("hour")
    val hour: List<HourRemote> = emptyList()
)