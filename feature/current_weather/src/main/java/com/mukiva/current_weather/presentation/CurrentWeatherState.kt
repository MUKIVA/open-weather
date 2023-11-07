package com.mukiva.current_weather.presentation

data class CurrentWeatherState(
    val title: String
) {
    companion object {
        fun default() = CurrentWeatherState(
            title = "Hello!"
        )
    }
}