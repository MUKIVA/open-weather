package com.github.mukiva.openweather.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

interface OpenWeatherTheme {

    @Composable
    fun colorScheme(): ColorScheme

    val lightColorScheme: ColorScheme @ReadOnlyComposable get

    val darkColorScheme: ColorScheme @ReadOnlyComposable get

    val shapes: Shapes

    val typography: Typography

    @Composable
    operator fun invoke(content: @Composable () -> Unit)

    companion object : OpenWeatherTheme by getPlatformOpenWeatherTheme()
}

expect fun getPlatformOpenWeatherTheme() : OpenWeatherTheme