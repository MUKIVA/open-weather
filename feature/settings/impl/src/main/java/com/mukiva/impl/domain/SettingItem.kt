package com.mukiva.impl.domain

abstract class SettingItem(
    val id: Int
) {

    data class Title(
        val name: String
    ) : SettingItem(TITLE_ITEM_ID)

    data class Toggle(
        val name: String,
        val description: String,
        val isEnabled: Boolean
    ) : SettingItem(TOGGLE_ITEM_ID)


    data class Variant(
        val name: String,
        val description: String,
        val variants: List<SettingVariant>
    ) : SettingItem(VARIANT_ITEM_ID)

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