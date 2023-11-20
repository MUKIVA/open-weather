package com.mukiva.impl.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.impl.domain.SettingItem

internal object SettingsDiffUtil : DiffUtil.ItemCallback<SettingItem>() {
    override fun areItemsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
        return oldItem == newItem
    }

}