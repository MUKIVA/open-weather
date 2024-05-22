package com.github.mukiva.feature.splash.presentation

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.splash.domain.AddLocationUseCase
import com.github.mukiva.feature.splash.navigation.ISplashRouter
import com.github.mukiva.weatherdata.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
    private val addLocationUseCase: AddLocationUseCase
) : ViewModel(), ISplashRouter by router {

    val state: StateFlow<OnboardingScreen>
        get() = mState

    val screenState: StateFlow<OnboardingScreenState>
        get() = mScreenState

    private val mState = MutableStateFlow(OnboardingScreen.entries.first())
    private val mScreenState = MutableStateFlow<OnboardingScreenState>(OnboardingScreenState.Content)

    private val mFirstOpenComplete = settingsRepository.getFirstOpenComplete()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    init {
        mFirstOpenComplete
            .onEach { isComplete -> if (isComplete) goDashboard() }
            .launchIn(viewModelScope)
    }

    fun handlePermissionGranted(permission: String, isGranted: Boolean) {
        viewModelScope.launch {
            val handler = viewModelScope.async {
                when (permission) {
                    Manifest.permission.POST_NOTIFICATIONS -> {
                        mScreenState.emit(OnboardingScreenState.Loading)
                        mScreenState.emit(OnboardingScreenState.Content)
                    }
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        mScreenState.emit(OnboardingScreenState.Loading)
                        if (isGranted) {
                            addLocationUseCase()
                        }
                        nextStep()
                        mScreenState.emit(OnboardingScreenState.Content)
                    }
                    else -> error("Permission not handled")
                }
            }
            handler.await()
            nextStep()
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
        mState.update { state -> OnboardingScreen.entries[state.ordinal.dec()] }
    }
}
