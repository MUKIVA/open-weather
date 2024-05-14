package com.github.mukiva.openweather.glue.locationmanager.navigation

import com.github.mukiva.feature.locationmanager.navigation.ILocationManagerRouter
import com.github.mukiva.navigation.router.GlobalRouter
import javax.inject.Inject

class LocationManagerRouter @Inject constructor(
    private val globalRouter: GlobalRouter
) : ILocationManagerRouter {
    override fun goBack() {
        globalRouter.navigateUp()
    }
}