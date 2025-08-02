package com.github.mukiva.openweather

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform