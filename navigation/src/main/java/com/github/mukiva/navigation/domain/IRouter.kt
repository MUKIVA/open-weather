package com.github.mukiva.navigation.domain

import android.os.Parcelable
import androidx.annotation.IdRes

interface IRouter {
    fun launch(@IdRes destination: Int, args: Parcelable? = null, setMainPage: Boolean = false)
    fun navigateUp(): Boolean
}
