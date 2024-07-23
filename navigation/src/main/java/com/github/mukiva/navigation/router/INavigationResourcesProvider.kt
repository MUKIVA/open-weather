package com.github.mukiva.navigation.router

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes

public interface INavigationResourcesProvider {

    @IdRes
    public fun provideStartDestination(): Int

    @NavigationRes
    public fun provideNavigationGraph(): Int
}
