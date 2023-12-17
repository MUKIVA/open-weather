package com.navigation

import com.mukiva.core.navigation.IBaseScreen
import com.mukiva.feature.dashboard_api.navigation.IDashboardScreenProvider

class DashboardScreenProvider : IDashboardScreenProvider {
    override val screen: IBaseScreen
        get() = DashboardScreen()
}