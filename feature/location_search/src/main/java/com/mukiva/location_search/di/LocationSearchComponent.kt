package com.mukiva.location_search.di

import com.mukiva.openweather.core.di.MultiViewModelFactory
import dagger.Component

@Component(
    dependencies = [LocationSearchDependencies::class],
    modules = [LocationSearchModule::class, LocationSearchBinds::class]
)
interface LocationSearchComponent {

    val factory: MultiViewModelFactory

    companion object {

        private var mInstance: LocationSearchComponent? = null

        private fun init(): LocationSearchComponent {
            mInstance = DaggerLocationSearchComponent.builder()
                .locationSearchDependencies(LocationSearchDepsProvider.deps)
                .build()
            return mInstance!!
        }

        fun get(): LocationSearchComponent {
            return mInstance ?: init()
        }
    }
}