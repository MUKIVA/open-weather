package com.mukiva.openweather.ui

import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

@MainThread
inline fun <reified TBind : ViewBinding> Fragment.viewBindings(
    noinline bindProvider: (view: View) -> TBind
): Lazy<TBind> {
    return lazy(LazyThreadSafetyMode.NONE) { bindProvider(this.requireView()) }
}

@MainThread
inline fun <reified T> Fragment.uiLazy(
    noinline objProvider: () -> T
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { objProvider() }
}