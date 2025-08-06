package com.github.mukiva.openweather.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

internal object IOSOpenWeatherTheme : OpenWeatherTheme {

    override val lightColorScheme: ColorScheme
        get() = lightColorScheme()
    override val darkColorScheme: ColorScheme
        get() = darkColorScheme()

    override val shapes: Shapes
        get() = Shapes()

    override val typography: Typography
        get() = Typography()

    @Composable
    override operator fun invoke(
        content: @Composable (() -> Unit)
    ) {
        MaterialTheme(
            colorScheme = colorScheme(),
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
    return IOSOpenWeatherTheme
}