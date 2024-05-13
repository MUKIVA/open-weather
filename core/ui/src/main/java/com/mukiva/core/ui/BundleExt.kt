@file:Suppress("UNCHECKED_CAST", "DEPRECATION")

package com.mukiva.core.ui

import android.os.Build
import android.os.Bundle
import java.io.Serializable

fun <T : Serializable> Bundle.getComaptSerializable(arg: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializable(arg, clazz)
    } else {
        this.getSerializable(arg) as T
    }
}
