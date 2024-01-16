package com.mukiva.feature.settings.domain

abstract class SettingItem(
    val id: Int,
    val group: Group
) {

    data class Title(
        val groupType: Group
    ) : SettingItem(TITLE_ITEM_ID, groupType)

    data class Toggle(
        val groupType: Group,
        val isEnabled: Boolean
    ) : SettingItem(TOGGLE_ITEM_ID, groupType)

    data class Variant(
        val groupType: Group,
        val variants: List<SettingVariant>
    ) : SettingItem(VARIANT_ITEM_ID, groupType)

    companion object {
        const val TITLE_ITEM_ID = 0
        const val TOGGLE_ITEM_ID = 1
        const val VARIANT_ITEM_ID = 2
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SettingItem) return false
        return when (this) {
            is Title -> (other as Title) == this
            is Variant -> (other as Variant) == this
            is Toggle -> (other as Toggle) == this
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id
    }

}