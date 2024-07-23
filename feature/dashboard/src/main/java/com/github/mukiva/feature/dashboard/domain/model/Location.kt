package com.github.mukiva.feature.dashboard.domain.model

internal data class Location(
    val id: Long,
    val name: String,
    val region: String,
    val country: String,
    val priority: Int
)