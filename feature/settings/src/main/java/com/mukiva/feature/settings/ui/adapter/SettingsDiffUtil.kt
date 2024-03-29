package com.mukiva.feature.settings.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.settings.domain.SettingItem

object SettingsDiffUtil : DiffUtil.ItemCallback<SettingItem>() {
    override fun areItemsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
        return oldItem == newItem
    }

}