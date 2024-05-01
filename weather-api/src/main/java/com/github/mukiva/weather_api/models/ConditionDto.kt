package com.github.mukiva.weather_api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConditionDto(
    @SerialName("text")
    var text: String,
    @SerialName("icon")
    var icon: String,
    @SerialName("code")
    var code: Int
)