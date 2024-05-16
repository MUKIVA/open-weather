package com.github.mukiva.navigation.domain

import androidx.annotation.IdRes
import java.io.Serializable

interface IRouter {
    fun launch(@IdRes destination: Int, args: Serializable? = null, setMainPage: Boolean = false)
    fun navigateUp(): Boolean
}
