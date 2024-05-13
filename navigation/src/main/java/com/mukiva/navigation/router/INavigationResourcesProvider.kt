package com.mukiva.navigation.router

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes

interface INavigationResourcesProvider {

    @IdRes
    fun provideStartDestination(): Int

    @NavigationRes
    fun provideNavigationGraph(): Int
}
