package com.mukiva.feature.forecast.domain.repository

interface IForecastUpdater {
    fun observeUpdate(block: () -> Unit)
}