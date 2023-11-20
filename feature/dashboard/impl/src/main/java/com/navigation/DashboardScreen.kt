package com.navigation

import androidx.fragment.app.Fragment
import com.mukiva.core.navigation.IBaseScreen
import com.ui.DashboardFragment

class DashboardScreen : IBaseScreen {
    override val fragment: Fragment
        get() = DashboardFragment.newInstance()
}