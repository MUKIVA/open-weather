package com.github.mukiva.openweather.core.data.domain.resources

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageResource(
    @SerialName("lang_name")
    val langName: String,
    @SerialName("lang_iso")
    val langIso: String,
    @SerialName("day_text")
    val dayText: String,
    @SerialName("night_text")
    val nightText: String
)