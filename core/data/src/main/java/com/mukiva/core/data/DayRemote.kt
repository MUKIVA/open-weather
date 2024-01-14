package com.mukiva.core.data

import com.google.gson.annotations.SerializedName

data class DayRemote(
    @SerializedName("maxtemp_c")
    val maxTempC: Double? = null,
    @SerializedName("maxtemp_f")
    val maxTempF: Double? = null,
    @SerializedName("mintemp_c")
    val minTempC: Double? = null,
    @SerializedName("mintemp_f")
    val minTempF: Double? = null,
    @SerializedName("avgtemp_c")
    val avgTempC: Double? = null,
    @SerializedName("avgtemp_f")
    val avgTempF: Double? = null,
    @SerializedName("maxwind_mph")
    val maxWindMph: Double? = null,
    @SerializedName("maxwind_kph")
    val maxWindKph: Double? = null,
    @SerializedName("totalprecip_mm")
    val totalPrecipMm: Double? = null,
    @SerializedName("totalprecip_in")
    val totalPrecipIn: Double? = null,
    @SerializedName("totalsnow_cm")
    val totalSnowCm: Double? = null,
    @SerializedName("avgvis_km")
    val avgVisKm: Double? = null,
    @SerializedName("avgvis_miles")
    val avgVisMiles: Double? = null,
    @SerializedName("avghumidity")
    val avgHumidity: Int? = null,
    @SerializedName("daily_will_it_rain")
    val dailyWillItRain: Int? = null,
    @SerializedName("daily_chance_of_rain")
    val dailyChanceOfRain: Int? = null,
    @SerializedName("daily_will_it_snow")
    val dailyWillItSnow: Int? = null,
    @SerializedName("daily_chance_of_snow")
    val dailyChanceOfSnow: Int? = null,
    @SerializedName("uv")
    val uv: Double? = null,
    @SerializedName("condition")
    val condition: ConditionRemote? = null
)