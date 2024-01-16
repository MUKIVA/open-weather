package com.mukiva.feature.dashboard.domain.model

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Float,
    val lon: Float,
)