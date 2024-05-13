package com.mukiva.feature.settings.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.settings.domain.SettingItem

object SettingsDiffUtil : DiffUtil.ItemCallback<SettingItem>() {
    override fun areItemsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
        return when {
            oldItem is SettingItem.Title && newItem is SettingItem.Title ->
                oldItem.group::class == newItem.group::class
            oldItem is SettingItem.Toggle && newItem is SettingItem.Toggle ->
                oldItem.key == newItem.key
            oldItem is SettingItem.Variant && newItem is SettingItem.Variant ->
                oldItem.key == newItem.key
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
        return oldItem == newItem
    }
}
