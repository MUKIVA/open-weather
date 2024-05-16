package com.github.mukiva.openweather.glue.splash.navigation

import com.github.mukiva.feature.splash.navigation.ISplashRouter
import com.github.mukiva.navigation.router.GlobalRouter
import com.github.mukiva.openweather.R
import javax.inject.Inject

class SplashRouter @Inject constructor(
    private val globalRouter: GlobalRouter
) : ISplashRouter {
    override fun goDashboard() {
        globalRouter.launch(R.id.dashboardFragment)
    }
}