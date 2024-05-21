package com.github.mukiva.feature.dashboard.presentation

import com.github.mukiva.weatherdata.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastStatesHolder @Inject constructor(
    private val locationRepository: LocationRepository
) {
    init {
        locationRepository.getAllLocal()
            .onEach { clear() }
            .launchIn(CoroutineScope(Job() + Dispatchers.IO))
    }

    private val mForecastStates = HashMap<Long, MutableStateFlow<LocationWeatherState>>()

    fun clear() = mForecastStates.keys.onEach { id ->
        mForecastStates[id]?.tryEmit(LocationWeatherState.Init)
    }

    operator fun get(id: Long) = mForecastStates[id]
        ?: MutableStateFlow<LocationWeatherState>(LocationWeatherState.Init).apply {
            mForecastStates[id] = this
        }
}
