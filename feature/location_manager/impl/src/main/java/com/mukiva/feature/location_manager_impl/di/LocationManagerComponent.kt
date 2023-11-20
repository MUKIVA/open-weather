package com.mukiva.feature.location_manager_impl.di

import com.mukiva.openweather.core.di.MultiViewModelFactory
import dagger.Component

@Component(
    dependencies = [LocationManagerDependencies::class],
    modules = [LocationManagerModule::class, LocationManagerBinds::class]
)
interface LocationManagerComponent {

    val factory: MultiViewModelFactory

    companion object {

        private var mInstance: LocationManagerComponent? = null

        private fun init(): LocationManagerComponent {
            mInstance = DaggerLocationManagerComponent.builder()
                .locationManagerDependencies(LocationManagerDepsProvider.deps)
                .build()
            return mInstance!!
        }

        fun get(): LocationManagerComponent {
            return mInstance ?: init()
        }
    }
}