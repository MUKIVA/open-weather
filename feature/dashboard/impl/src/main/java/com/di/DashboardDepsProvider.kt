package com.di

interface DashboardDepsProvider {
    var deps: DashboardDependencies

    companion object : DashboardDepsProvider by DashboardDepsStore
}