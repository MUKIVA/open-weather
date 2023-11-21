package com.di

import com.mukiva.openweather.core.di.FeatureScope
import com.mukiva.openweather.core.di.MultiViewModelFactory
import com.ui.DashboardFragment
import dagger.Component

@[FeatureScope Component(
    modules = [DashboardModule::class, IDashboardBinds::class],
    dependencies = [IDashboardDependencies::class]
)]
interface IDashboardComponent {

    val factory: MultiViewModelFactory

    fun inject(fragment: DashboardFragment)

    companion object {

        private var mInstance: IDashboardComponent? = null

        fun init(deps: IDashboardDependencies) {
            mInstance = DaggerIDashboardComponent.builder()
                .iDashboardDependencies(deps)
                .build()
        }

        fun get(): IDashboardComponent {
            return mInstance!!
        }
    }
}





