package com.github.mukiva.openweather.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SportDto(
    @SerialName("stadium")
    val stadium: String?,
    @SerialName("country")
    val country: Int?,
    @SerialName("region")
    val region: String?,
    @SerialName("tournament")
    val tournament: String?,
    @SerialName("start")
    val start: String?,
    @SerialName("match")
    val match: String?
)