package com.mukiva.feature.settings_impl.domain

import java.io.Serializable

class SettingVariantList(
    val title: String,
    val list: List<SettingVariant>
) : Serializable