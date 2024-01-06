package com.mukiva.openweather.ui

import androidx.fragment.app.Fragment


fun Fragment.dp(dp: Int): Int {
    return (dp * (requireContext().resources.displayMetrics.density + 0.45)).toInt()
}


//val Int.dp: Int
//    get() = (this * Resources.getSystem().displayMetrics.densityDpi)

//val Float.dp: Int
//    get() = (this * Resources.getSystem().displayMetrics.densityDpi).toInt()

//val Int.dp: Int
//    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
//
//val Float.dp: Int
//    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()