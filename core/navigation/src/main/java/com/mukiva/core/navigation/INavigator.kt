package com.mukiva.core.navigation

import androidx.annotation.StringRes

interface INavigator {
    fun launch(screen: IBaseScreen)
    fun goBack()
    fun getString(@StringRes msgRes: Int): String
}