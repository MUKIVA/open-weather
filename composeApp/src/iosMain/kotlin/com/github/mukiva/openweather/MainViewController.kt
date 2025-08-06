package com.github.mukiva.openweather

import androidx.compose.ui.window.ComposeUIViewController
import com.github.mukiva.openweather.di.commonGlobalModule
import com.github.mukiva.openweather.di.iosGlobalModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    startKoin {
        modules(iosGlobalModule + commonGlobalModule)
    }

    return ComposeUIViewController {
        App()
    }
}