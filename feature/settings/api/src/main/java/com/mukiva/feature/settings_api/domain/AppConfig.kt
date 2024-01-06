package com.mukiva.feature.settings_api.domain

import com.mukiva.feature.settings_api.UnitsType
import com.mukiva.feature.settings_api.Theme

data class AppConfig(
    val theme: Theme,
    val unitsType: UnitsType
)