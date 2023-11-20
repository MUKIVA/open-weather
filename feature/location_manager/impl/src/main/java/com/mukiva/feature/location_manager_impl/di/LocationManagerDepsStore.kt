package com.mukiva.feature.location_manager_impl.di

import kotlin.properties.Delegates

object LocationManagerDepsStore : LocationManagerDepsProvider {
    override var deps: LocationManagerDependencies by Delegates.notNull()
}