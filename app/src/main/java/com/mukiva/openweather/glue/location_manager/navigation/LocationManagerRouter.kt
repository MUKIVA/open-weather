package com.mukiva.openweather.glue.location_manager.navigation

import com.mukiva.feature.location_manager.navigation.ILocationManagerRouter
import com.mukiva.navigation.router.GlobalRouter
import javax.inject.Inject

class LocationManagerRouter @Inject constructor(
    private val globalRouter: GlobalRouter
) : ILocationManagerRouter {
    override fun goBack() {
        globalRouter.navigateUp()
    }
}