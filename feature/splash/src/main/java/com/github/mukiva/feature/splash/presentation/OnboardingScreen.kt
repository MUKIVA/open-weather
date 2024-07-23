package com.github.mukiva.feature.splash.presentation

internal enum class OnboardingScreen {
    NOTIFICATION_ACCESS,
    LOCATION_ACCESS,
}

internal sealed class OnboardingScreenState {
    data object Content : OnboardingScreenState()
    data object Loading : OnboardingScreenState()
}
