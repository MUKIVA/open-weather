package com.github.mukiva.feature.dashboard.navigation

interface IDashboardRouter {
    fun goFullForecast(locationId: Long, dayPosition: Int)
    fun goLocationManager()
    fun goSettings()
    fun goBack()
}
