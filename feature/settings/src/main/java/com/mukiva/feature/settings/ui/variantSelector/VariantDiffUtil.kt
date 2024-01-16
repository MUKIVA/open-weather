package com.mukiva.feature.settings.ui.variantSelector

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.settings.domain.SettingVariant

object VariantDiffUtil : DiffUtil.ItemCallback<SettingVariant>() {
    override fun areItemsTheSame(oldItem: SettingVariant, newItem: SettingVariant): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SettingVariant, newItem: SettingVariant): Boolean {
        return oldItem == newItem
    }
}