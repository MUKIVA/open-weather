package com.github.mukiva.feature.splash.presentation

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.splash.navigation.ISplashRouter
import com.github.mukiva.weatherdata.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    router: ISplashRouter,
    private val settingsRepository: SettingsRepository,
) : ViewModel(), ISplashRouter by router {

    val state: StateFlow<OnboardingScreen>
        get() = mState

    private val mState = MutableStateFlow(OnboardingScreen.entries.first())

    private val mFirstOpenComplete = settingsRepository.getFirstOpenComplete()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    init {
        mFirstOpenComplete
            .onEach { isComplete -> if(isComplete) goDashboard() }
            .launchIn(viewModelScope)
    }

    fun handlePermissionGranted(permission: String) {
        when (permission) {
            Manifest.permission.POST_NOTIFICATIONS -> {
            }
            Manifest.permission.ACCESS_COARSE_LOCATION -> {
            }
            else -> error("Permission not handled")
        }
    }

    fun nextStep() {
        when (mState.value) {
            OnboardingScreen.entries.last() -> {
                viewModelScope.launch {
                    settingsRepository.setFirstOpenComplete(true)
                }
                goDashboard()
            }
            else -> mState.update { OnboardingScreen.entries[it.ordinal + 1] }
        }
    }

    fun previousStep() {
        mState.update { OnboardingScreen.entries[it.ordinal - 1] }
    }
}
