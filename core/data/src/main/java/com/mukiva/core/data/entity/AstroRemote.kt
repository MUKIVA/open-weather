package com.mukiva.core.data.entity

import com.google.gson.annotations.SerializedName

data class AstroRemote(
    @SerializedName("sunrise")
    val sunrise: String? = null,
    @SerializedName("sunset")
    val sunset: String? = null,
    @SerializedName("moonrise")
    val moonrise: String? = null,
    @SerializedName("moonset")
    val moonset: String? = null,
    @SerializedName("moon_phase")
    val moonPhase: String? = null,
    @SerializedName("moon_illumination")
    val moonIllumination: Int? = null,
    @SerializedName("is_moon_up")
    val isMoonUp: Int? = null,
    @SerializedName("is_sun_up")
    val isSunUp: Int? = null
)