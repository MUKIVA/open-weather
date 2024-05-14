package com.github.mukiva.feature.dashboard.navigation

interface IDashboardRouter {
    fun goFullForecast(locationName: String, dayPosition: Int)
    fun goLocationManager()
    fun goSettings()
    fun goBack()
}
