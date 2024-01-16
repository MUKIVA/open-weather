package com.mukiva.feature.settings.domain

import java.io.Serializable

class SettingVariantList(
    val title: String,
    val list: List<SettingVariant>
) : Serializable