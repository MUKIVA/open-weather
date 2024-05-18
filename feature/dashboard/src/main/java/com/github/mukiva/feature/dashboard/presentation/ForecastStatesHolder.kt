package com.github.mukiva.feature.dashboard.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastStatesHolder @Inject constructor() {

    private val mForecastStates = HashMap<Long, MutableStateFlow<LocationWeatherState>>()

    fun clear() = mForecastStates.keys.onEach { id ->
        mForecastStates[id]?.tryEmit(LocationWeatherState.Init)
    }

    operator fun get(id: Long) = mForecastStates[id]
        ?: MutableStateFlow<LocationWeatherState>(LocationWeatherState.Init).apply {
            mForecastStates[id] = this
        }
}
