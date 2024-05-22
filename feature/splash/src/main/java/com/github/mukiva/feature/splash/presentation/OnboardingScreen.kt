package com.github.mukiva.feature.splash.presentation

enum class OnboardingScreen {
    NOTIFICATION_ACCESS,
    LOCATION_ACCESS,
}

sealed class OnboardingScreenState {
    data object Content : OnboardingScreenState()
    data object Loading : OnboardingScreenState()
}
