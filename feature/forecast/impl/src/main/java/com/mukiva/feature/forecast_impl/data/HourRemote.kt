package com.mukiva.feature.forecast_impl.data

import com.google.gson.annotations.SerializedName
import com.mukiva.core.data.ConditionRemote

data class HourRemote(
    @SerializedName("time_epoch")
    val timeEpoch: Long? = null,
    @SerializedName("time")
    val time: String? = null,
    @SerializedName("temp_c")
    val tempC: Double? = null,
    @SerializedName("temp_f")
    val tempF: Double? = null,
    @SerializedName("is_day")
    val isDay: Int? = null,
    @SerializedName("wind_mph")
    val windMph: Double? = null,
    @SerializedName("wind_kph")
    val windKph: Double? = null,
    @SerializedName("wind_degree")
    val windDegree: Int? = null,
    @SerializedName("wind_dir")
    val windDir: String? = null,
    @SerializedName("pressure_mb")
    val pressureMb: Double? = null,
    @SerializedName("pressure_in")
    val pressureIn: Double? = null,
    @SerializedName("precip_mm")
    val precipMm: Double? = null,
    @SerializedName("precip_in")
    val precipIn: Double? = null,
    @SerializedName("snow_cm")
    val snowCm: Double? = null,
    @SerializedName("humidity")
    val humidity: Int? = null,
    @SerializedName("cloud")
    val cloud: Int? = null,
    @SerializedName("feelslike_c")
    val feelsLikeC: Double? = null,
    @SerializedName("feelslike_f")
    val feelsLikeF: Double? = null,
    @SerializedName("windchill_c")
    val windChillC: Double? = null,
    @SerializedName("windchill_f")
    val windChillF: Double? = null,
    @SerializedName("heatindex_c")
    val heatIndexC: Double? = null,
    @SerializedName("heatindex_f")
    val heatIndexF: Double? = null,
    @SerializedName("dewpoint_c")
    val dewPointC: Double? = null,
    @SerializedName("dewpoint_f")
    val dewPointF: Double? = null,
    @SerializedName("will_it_rain")
    val willItRain: Int? = null,
    @SerializedName("chance_of_rain")
    val chanceOfRain: Int? = null,
    @SerializedName("will_it_snow")
    val willItSnow: Int? = null,
    @SerializedName("chance_of_snow")
    val chanceOfSnow: Int? = null,
    @SerializedName("vis_km")
    val visKm: Double? = null,
    @SerializedName("vis_miles")
    val visMiles: Double? = null,
    @SerializedName("gust_mph")
    val gustMph: Double? = null,
    @SerializedName("gust_kph")
    val gustKph: Double? = null,
    @SerializedName("uv")
    val uv: Double? = null,
    @SerializedName("condition")
    val condition: ConditionRemote? = null
)