package com.github.mukiva.navigation.domain

import android.os.Parcelable
import androidx.annotation.IdRes

public interface IRouter {
    public fun launch(@IdRes destination: Int, args: Parcelable? = null, setMainPage: Boolean = false)
    public fun navigateUp(): Boolean
}
