package com.github.mukiva.openweather.core.data.domain.resources

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConditionResource(
    @SerialName("code")
    val code: Int,
    @SerialName("day")
    val day: String,
    @SerialName("night")
    val night: String,
    @SerialName("icon")
    val icon: Int,
    @SerialName("languages")
    val languages: List<LanguageResource>
)