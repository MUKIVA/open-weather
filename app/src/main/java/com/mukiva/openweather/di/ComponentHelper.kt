package com.mukiva.openweather.di

import com.di.IDashboardComponent
import com.mukiva.feature.location_manager_impl.di.ILocationManagerComponent
import com.mukiva.impl.di.ISettingsComponent
import com.mukiva.openweather.deps.DashboardDeps
import com.mukiva.openweather.deps.LocationManagerDeps
import com.mukiva.openweather.deps.SettingsDeps

object ComponentHelper {

    fun initAll() {
        IDashboardComponent.init(DashboardDeps)
        ILocationManagerComponent.init(LocationManagerDeps)
        ISettingsComponent.init(SettingsDeps)
    }

}