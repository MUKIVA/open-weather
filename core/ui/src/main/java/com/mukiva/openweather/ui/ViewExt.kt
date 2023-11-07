package com.mukiva.openweather.ui

import android.view.View
import androidx.core.view.isVisible

fun View.gone() { isVisible = false }

fun View.visible() { isVisible = true }