package com.mukiva.feature.dashboard.domain.repository

interface IForecastUpdater {
    fun observeUpdate(block: () -> Unit)
}