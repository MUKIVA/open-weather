package com.mukiva.feature.location_manager_impl.navigation

import com.mukiva.core.navigation.IBaseScreen
import com.mukiva.feature.location_manager_api.navigation.ILocationManagerScreenProvider
import javax.inject.Inject

class LocationManagerScreenProvider @Inject constructor() : ILocationManagerScreenProvider {
    override val screen: IBaseScreen
        get() = LocationManagerScreen()
}