package com.mukiva.core.navigation

interface INavigator {
    fun launch(screen: IBaseScreen)
    fun goBack()
}