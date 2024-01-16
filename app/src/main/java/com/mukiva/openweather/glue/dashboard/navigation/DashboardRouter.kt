package com.mukiva.openweather.glue.dashboard.navigation

import com.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.mukiva.navigation.router.GlobalRouter
import com.mukiva.openweather.R
import javax.inject.Inject

class DashboardRouter @Inject constructor(
    private val globalRouter: GlobalRouter
) : IDashboardRouter {
    override fun goLocationManager() {
        globalRouter.launch(R.id.locationManagerFragment)
    }

    override fun goSettings() {
        globalRouter.launch(R.id.settingsTemplateFragment)
    }

    override fun goBack() {
        globalRouter.navigateUp()
    }
}