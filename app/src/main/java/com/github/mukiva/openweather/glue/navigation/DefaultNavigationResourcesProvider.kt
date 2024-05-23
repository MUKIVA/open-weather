package com.github.mukiva.openweather.glue.navigation

import com.github.mukiva.navigation.router.INavigationResourcesProvider
import com.github.mukiva.openweather.R

class DefaultNavigationResourcesProvider : INavigationResourcesProvider {
    override fun provideStartDestination(): Int {
        return R.id.dashboardFragment
    }

    override fun provideNavigationGraph(): Int {
        return R.navigation.navigation_main
    }
}

