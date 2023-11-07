package com.mukiva.location_search.di

interface LocationSearchDepsProvider {
    var deps: LocationSearchDependencies

    companion object : LocationSearchDepsProvider by LocationSearchDepsStore
}