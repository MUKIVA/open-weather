package com.mukiva.openweather.glue.navigation

import com.mukiva.feature.settings.domain.config.Theme
import com.mukiva.navigation.domain.Theme as TargetTheme
object ThemeMapper {

    fun map(coreItem: Theme): TargetTheme = when(coreItem) {
        Theme.SYSTEM -> TargetTheme.SYSTEM
        Theme.DARK -> TargetTheme.DARK
        Theme.LIGHT -> TargetTheme.LIGHT
    }

}