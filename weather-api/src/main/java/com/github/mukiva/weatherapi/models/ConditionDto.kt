package com.github.mukiva.weatherapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ConditionDto(
    @SerialName("text")
    var text: String,
    @SerialName("icon")
    var icon: String,
    @SerialName("code")
    var code: Int
)
