package com.github.mukiva.openweather.core.theme

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

internal object AndroidOpenWeatherTheme : OpenWeatherTheme {

    override val lightColorScheme: ColorScheme
        @ReadOnlyComposable get() = lightColorScheme()
    override val darkColorScheme: ColorScheme
        @ReadOnlyComposable get() = darkColorScheme()
    override val shapes: Shapes
        get() = Shapes()
    override val typography: Typography
        get() = Typography()

    @SuppressLint("ComposableNaming")
    @Composable
    override operator fun invoke(
        content: @Composable (() -> Unit)
    ) {
        val isDarkTheme = isSystemInDarkTheme()

        val colorScheme = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (isDarkTheme)
                    dynamicDarkColorScheme(context)
                else
                    dynamicLightColorScheme(context)
            }

            else -> colorScheme()
        }

        MaterialTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            typography = typography,
            content = content
        )
    }

    @Composable
    override fun colorScheme(): ColorScheme {
        return when (isSystemInDarkTheme()) {
            true -> darkColorScheme
            false -> lightColorScheme
        }
    }

}


actual fun getPlatformOpenWeatherTheme(): OpenWeatherTheme {
    return AndroidOpenWeatherTheme
}