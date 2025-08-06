package com.github.mukiva.openweather.core.theme

enum class Platform(val id: String) {
    ANDROID("android"),
    IOS("ios")
}

expect fun getPlatform(): Platform