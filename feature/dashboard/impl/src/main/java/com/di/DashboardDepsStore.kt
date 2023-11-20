package com.di

import kotlin.properties.Delegates

object DashboardDepsStore : DashboardDepsProvider {
    override var deps: DashboardDependencies by Delegates.notNull()
}