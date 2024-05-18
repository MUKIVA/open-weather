package com.github.mukiva.openweather.glue.dashboard.navigation

import com.github.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.github.mukiva.feature.forecast.ui.ForecastFragment
import com.github.mukiva.navigation.router.GlobalRouter
import com.github.mukiva.openweather.R
import javax.inject.Inject

class DashboardRouter @Inject constructor(
    private val globalRouter: GlobalRouter
) : IDashboardRouter {

    override fun goFullForecast(locationId: Long, dayPosition: Int) {
        globalRouter.launch(
            R.id.forecastFragment,
            ForecastFragment.Args(
                locationId = locationId,
                dayPosition = dayPosition
            )
        )
    }

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
