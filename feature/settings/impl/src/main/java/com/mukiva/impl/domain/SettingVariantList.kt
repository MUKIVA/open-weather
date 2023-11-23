package com.mukiva.impl.domain

import java.io.Serializable

class SelectVariantState(
    val title: String,
    val list: List<SettingVariant>
) : Serializable