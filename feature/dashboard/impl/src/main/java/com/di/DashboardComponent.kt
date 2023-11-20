package com.di

import com.mukiva.openweather.core.di.FeatureScope
import com.mukiva.openweather.core.di.MultiViewModelFactory
import com.ui.DashboardFragment
import dagger.Component

@[FeatureScope Component(
    modules = [DashboardModule::class, DashboardBinds::class],
    dependencies = [DashboardDependencies::class]
)]
internal interface DashboardComponent {

    val factory: MultiViewModelFactory

    fun inject(fragment: DashboardFragment)

    companion object {

        private var mInstance: DashboardComponent? = null

        private fun init(): DashboardComponent {
            mInstance = DaggerDashboardComponent.builder()
                .dashboardDependencies(DashboardDepsProvider.deps)
                .build()
            return mInstance!!
        }

        fun get(): DashboardComponent {
            return mInstance ?: init()
        }
    }
}





