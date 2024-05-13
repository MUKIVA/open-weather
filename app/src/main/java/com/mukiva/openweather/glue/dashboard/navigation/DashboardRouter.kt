package com.mukiva.openweather.glue.dashboard.navigation

import com.mukiva.feature.dashboard.navigation.IDashboardRouter
import com.mukiva.feature.forecast.ui.ForecastFragment
import com.mukiva.navigation.router.GlobalRouter
import com.mukiva.openweather.R
import javax.inject.Inject

class DashboardRouter @Inject constructor(
    private val globalRouter: GlobalRouter
) : IDashboardRouter {

    override fun goFullForecast(locationName: String, dayPosition: Int) {
        globalRouter.launch(
            R.id.forecastFragment,
            ForecastFragment.Args(
                locationName = locationName,
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
