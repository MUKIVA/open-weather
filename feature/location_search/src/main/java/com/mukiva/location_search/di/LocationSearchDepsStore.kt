package com.mukiva.location_search.di

import kotlin.properties.Delegates

object LocationSearchDepsStore : LocationSearchDepsProvider {
    override var deps: LocationSearchDependencies by Delegates.notNull()
}