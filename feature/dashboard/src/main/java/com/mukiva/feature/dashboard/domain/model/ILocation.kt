package com.mukiva.feature.dashboard.domain.model

interface ILocation {
    val name: String
    val region: String
    val country: String
    val lat: Float
    val lon: Float
    val position: Int
}