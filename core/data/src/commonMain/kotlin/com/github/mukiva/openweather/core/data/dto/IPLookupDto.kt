package com.github.mukiva.openweather.core.data.dto

import com.github.mukiva.openweather.core.data.domain.common.IPType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IPLookupDto(
    @SerialName("ip")
    val ip: String?,
    @SerialName("type")
    val type: IPType?,
    @SerialName("continent_code")
    val continentCode: String?,
    @SerialName("continent_name")
    val continentName: String?,
    @SerialName("country_code")
    val countryCode: String?,
    @SerialName("country_name")
    val countryName: String?,
    @SerialName("is_eu")
    val isEurope: Boolean?,
    @SerialName("geoname_id")
    val geoNameId: String?,
    @SerialName("city")
    val city: String?,
    @SerialName("region")
    val region: String?,
    @SerialName("lat")
    val lat: Double?,
    @SerialName("lon")
    val lon: Double?,
    @SerialName("tz_id")
    val timeZoneId: String?
)