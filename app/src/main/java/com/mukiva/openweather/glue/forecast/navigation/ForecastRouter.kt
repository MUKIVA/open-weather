package com.mukiva.openweather.glue.forecast.navigation

import com.mukiva.feature.forecast.navigation.IForecastRouter
import com.mukiva.feature.forecast.ui.ForecastFragment
import com.mukiva.navigation.router.GlobalRouter
import com.mukiva.openweather.R
import javax.inject.Inject

class ForecastRouter @Inject constructor(
    private val globalRouter: GlobalRouter
) : IForecastRouter {
    override fun goFullForecast(locationName: String, dayPosition: Int) {
        globalRouter.launch(R.id.forecastFragment, ForecastFragment.Args(
            locationName = locationName,
            dayPosition = dayPosition
        ))
    }
}