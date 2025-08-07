package com.github.mukiva.openweather.presentation

internal interface CommonRootComponent {

    enum class Platform {
        ANDROID,
        IOS
    }

    interface Child

    val platform: Platform

}