package com.mukiva.impl.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mukiva.feature.settings_impl.databinding.ItemSettingsGroupTitleBinding
import com.mukiva.feature.settings_impl.databinding.ItemSettingsToggleBinding
import com.mukiva.feature.settings_impl.databinding.ItemSettingsVariantBinding
import com.mukiva.impl.domain.SettingItem

internal class SettingsAdapter : ListAdapter<SettingItem, ViewHolder>(SettingsDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            SettingItem.TITLE_ITEM_ID -> {
                SettingsViewHolder.TitleViewHolder(
                    binding = ItemSettingsGroupTitleBinding.inflate(inflater, parent, false)
                )
            }
            SettingItem.VARIANT_ITEM_ID -> {
                SettingsViewHolder.VariantViewHolder(
                    binding = ItemSettingsVariantBinding.inflate(inflater, parent, false)
                )
            }
            SettingItem.TOGGLE_ITEM_ID -> {
                SettingsViewHolder.ToggleViewHolder(
                    binding = ItemSettingsToggleBinding.inflate(inflater, parent, false)
                )
            }
            else -> throw IllegalStateException("Сan't create a holder with $viewType type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder !is SettingsViewHolder) return
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).id
    }

}
