package com.github.mukiva.feature.settings.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.mukiva.feature.settings.databinding.ItemSettingsGroupTitleBinding
import com.github.mukiva.feature.settings.databinding.ItemSettingsToggleBinding
import com.github.mukiva.feature.settings.databinding.ItemSettingsVariantBinding
import com.github.mukiva.feature.settings.domain.SettingItem
import kotlin.reflect.KClass

internal class SettingsAdapter(
    private val onSelectVariant: (SettingItem.Variant) -> Unit,
    private val onToggleOption: (KClass<*>, Boolean) -> Unit
) : ListAdapter<SettingItem, ViewHolder>(SettingsDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            SettingItem.TITLE_ITEM_ID -> {
                ISettingsViewHolder.TitleViewHolder(
                    binding = ItemSettingsGroupTitleBinding.inflate(inflater, parent, false)
                )
            }
            SettingItem.VARIANT_ITEM_ID -> {
                ISettingsViewHolder.VariantViewHolder(
                    binding = ItemSettingsVariantBinding.inflate(inflater, parent, false),
                    onClick = onSelectVariant
                )
            }
            SettingItem.TOGGLE_ITEM_ID -> {
                ISettingsViewHolder.ToggleViewHolder(
                    binding = ItemSettingsToggleBinding.inflate(inflater, parent, false),
                    onClick = onToggleOption
                )
            }
            else -> error("Сan not create a holder with $viewType type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder !is ISettingsViewHolder) return
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).id
    }

}
