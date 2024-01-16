package com.mukiva.navigation.domain

import androidx.annotation.IdRes
import java.io.Serializable

interface IRouter {
    fun launch(@IdRes destination: Int, args: Serializable? = null)
    fun navigateUp(): Boolean
}