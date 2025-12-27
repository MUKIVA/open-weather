package com.github.mukiva.openweather.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlertDto(
    @SerialName("headline")
    val headline: String?,
    @SerialName("msgType")
    val msgType: String?,
    @SerialName("severity")
    val severity: String?,
    @SerialName("urgency")
    val urgency: String?,
    @SerialName("areas")
    val areas: String?,
    @SerialName("category")
    val category: String?,
    @SerialName("certainty")
    val certainty: String?,
    @SerialName("event")
    val event: String?,
    @SerialName("note")
    val note: String?,
    @SerialName("effective")
    val effective: String?,
    @SerialName("expires")
    val expires: String?,
    @SerialName("desc")
    val desc: String?,
    @SerialName("instruction")
    val instruction: String?
)