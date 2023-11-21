package com.mukiva.feature.location_manager_impl.navigation

import androidx.fragment.app.Fragment
import com.mukiva.core.navigation.IBaseScreen
import com.mukiva.feature.location_manager_impl.ui.LocationManagerFragment

class LocationManagerScreen : IBaseScreen {
    override val fragment: Fragment
        get() = LocationManagerFragment.newInstance()
}