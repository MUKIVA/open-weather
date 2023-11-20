package com.mukiva.feature.location_manager_impl.di

interface LocationManagerDepsProvider {
    var deps: LocationManagerDependencies
    companion object : LocationManagerDepsProvider by LocationManagerDepsStore
}