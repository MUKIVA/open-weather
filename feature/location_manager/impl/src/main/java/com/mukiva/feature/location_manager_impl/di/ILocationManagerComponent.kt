package com.mukiva.feature.location_manager_impl.di

import com.mukiva.openweather.core.di.MultiViewModelFactory
import dagger.Component

@Component(
    dependencies = [ILocationManagerDependencies::class],
    modules = [LocationManagerModule::class, ILocationManagerBinds::class]
)
interface ILocationManagerComponent {

    val factory: MultiViewModelFactory

    companion object {

        private var mInstance: ILocationManagerComponent? = null

        fun init(deps: ILocationManagerDependencies) {
            mInstance = DaggerILocationManagerComponent.builder()
                .iLocationManagerDependencies(deps)
                .build()
        }

        fun get(): ILocationManagerComponent {
            return mInstance!!
        }
    }
}