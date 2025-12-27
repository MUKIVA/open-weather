package com.github.mukiva.openweather.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConditionDto(
    @SerialName("text")
    val text: String?,
    @SerialName("icon")
    val icon: String?,
    @SerialName("code")
    val code: Int?
)