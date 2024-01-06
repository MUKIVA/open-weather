package com.mukiva.feature.dashboard_impl.navigation

import androidx.fragment.app.Fragment
import com.mukiva.core.navigation.IBaseScreen
import com.mukiva.feature.dashboard_impl.ui.DashboardFragment

class DashboardScreen : IBaseScreen {
    override val fragment: Fragment
        get() = DashboardFragment.newInstance()
}