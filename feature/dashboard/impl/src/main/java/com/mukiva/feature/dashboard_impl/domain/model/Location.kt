package com.mukiva.feature.dashboard_impl.domain.model

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Float,
    val lon: Float,
)