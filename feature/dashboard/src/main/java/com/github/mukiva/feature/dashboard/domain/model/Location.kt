package com.github.mukiva.feature.dashboard.domain.model

data class Location(
    val id: Long,
    val name: String,
    val region: String,
    val country: String,
    val priority: Int
)