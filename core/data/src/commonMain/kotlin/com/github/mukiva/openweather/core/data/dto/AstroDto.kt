package com.github.mukiva.openweather.core.data.dto

import com.github.mukiva.openweather.core.data.domain.common.MoonPhase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AstroDto(
    @SerialName("sunrise")
    val sunrise: String?,
    @SerialName("sunset")
    val sunset: String?,
    @SerialName("moonrise")
    val moonrise: String?,
    @SerialName("moonset")
    val moonset: String?,
    @SerialName("moon_phase")
    val moonPhase: MoonPhase?,
    @SerialName("moon_illumination")
    val moonIllumination: Double?,
    @SerialName("is_moon_up")
    val isMoonUp: Int?,
    @SerialName("is_sun_up")
    val isSunUp: Int?
)
