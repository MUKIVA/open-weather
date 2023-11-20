package com.presentation

data class DashboardState(
    val title: String
) {
    companion object {
        fun default() = DashboardState(
            title = "Hello!"
        )
    }
}