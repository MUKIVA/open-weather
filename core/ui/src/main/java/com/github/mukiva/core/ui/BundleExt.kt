@file:Suppress("DEPRECATION")

package com.github.mukiva.core.ui

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

internal fun <T : Parcelable> Bundle.getCompatParcelable(arg: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(arg, clazz)
    } else {
        getParcelable(arg)
    }
}
