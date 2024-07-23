package com.github.mukiva.feature.settings.domain

import com.github.mukiva.openweather.core.domain.settings.Group
import kotlin.enums.EnumEntries
import kotlin.reflect.KClass

internal sealed class SettingItem(
    val id: Int
) {
    data class Title(val group: Group) : SettingItem(TITLE_ITEM_ID)
    data class Toggle(
        val key: KClass<*>,
        val isEnabled: Boolean
    ) : SettingItem(TOGGLE_ITEM_ID)
    data class Variant(
        val key: KClass<*>,
        val selectedVariant: Enum<*>,
        val variants: EnumEntries<*>,
    ) : SettingItem(VARIANT_ITEM_ID)
    companion object {
        const val TITLE_ITEM_ID = 0
        const val TOGGLE_ITEM_ID = 1
        const val VARIANT_ITEM_ID = 2
    }
}
