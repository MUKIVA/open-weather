package com.mukiva.current_weather.domain.model

import java.time.LocalDateTime

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
) {
    companion object {
        fun default() = Condition(
            text = "Unknown",
            icon = "Unknown",
            code = 0
        )
    }
}


