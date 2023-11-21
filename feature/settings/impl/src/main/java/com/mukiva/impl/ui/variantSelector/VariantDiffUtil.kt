package com.mukiva.impl.ui.variantSelector

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.impl.domain.SettingVariant

object VariantDiffUtil : DiffUtil.ItemCallback<SettingVariant>() {
    override fun areItemsTheSame(oldItem: SettingVariant, newItem: SettingVariant): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SettingVariant, newItem: SettingVariant): Boolean {
        return oldItem == newItem
    }
}