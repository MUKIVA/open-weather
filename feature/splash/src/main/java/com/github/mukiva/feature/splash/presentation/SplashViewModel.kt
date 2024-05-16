package com.github.mukiva.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mukiva.feature.splash.navigation.ISplashRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashState {
    data object Init : SplashState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    router: ISplashRouter
) : ViewModel(), ISplashRouter by router {
    val state: StateFlow<SplashState>
        get() = mState.asStateFlow()

    private val mState = MutableStateFlow<SplashState>(SplashState.Init)

    fun load() {
        viewModelScope.launch {
            delay(1000)
            goDashboard()
        }
    }
}
