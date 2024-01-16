//package com.mukiva.openweather.di
//
//import android.app.Application
//import com.mukiva.core.navigation.INavigator
////import com.mukiva.openweather.navigator.MainNavigator
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class NavigationModule {
//    @Provides
//    @Singleton
//    fun provideNavigator(
//        application: Application
//    ): INavigator = MainNavigator(application)
//
//}