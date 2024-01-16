package com.mukiva.feature.settings.domain

import java.io.Serializable

data class SettingVariant(
    val name: String,
    val isSelected: Boolean,
) : Serializable