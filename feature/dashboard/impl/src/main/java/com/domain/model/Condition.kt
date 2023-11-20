package com.domain.model

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


