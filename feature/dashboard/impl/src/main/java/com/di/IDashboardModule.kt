package com.di

import com.mukiva.feature.dashboard_api.navigation.IDashboardScreenProvider
import com.navigation.DashboardScreenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IDashboardModule {

    @Binds
    fun bindDashboardScreenProvider(
        provider: DashboardScreenProvider
    ): IDashboardScreenProvider

}