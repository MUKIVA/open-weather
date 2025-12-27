package com.github.mukiva.openweather.core.data.domain.common

import kotlinx.serialization.Serializable

@Serializable
enum class IPType(val id: String) {
    IPV4("ipv4"),
    IPV6("ipv6")
}