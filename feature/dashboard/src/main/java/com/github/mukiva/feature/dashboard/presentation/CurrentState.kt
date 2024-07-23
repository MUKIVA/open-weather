package com.github.mukiva.feature.dashboard.presentation

import com.github.mukiva.feature.dashboard.domain.model.Astro
import com.github.mukiva.feature.dashboard.domain.model.Current
import com.github.mukiva.feature.dashboard.domain.model.Precipitation

sealed interface ICurrentState {
    data object Init : ICurrentState
    data object Loading : ICurrentState
    data object Error : ICurrentState
    data class Content(
        val currentState: Current,
        val precipitation: Precipitation,
        val astro: Astro,
        val forecastState: ForecastState
    ) : ICurrentState
}