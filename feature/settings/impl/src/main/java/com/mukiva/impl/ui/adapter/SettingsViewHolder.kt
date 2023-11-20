package com.mukiva.impl.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.settings_impl.R
import com.mukiva.feature.settings_impl.databinding.ItemSettingsGroupTitleBinding
import com.mukiva.feature.settings_impl.databinding.ItemSettingsToggleBinding
import com.mukiva.feature.settings_impl.databinding.ItemSettingsVariantBinding
import com.mukiva.impl.domain.SettingItem
import com.mukiva.impl.domain.SettingVariant

internal sealed interface SettingsViewHolder {
    fun bind(item: SettingItem)

    class TitleViewHolder(
        private val binding: ItemSettingsGroupTitleBinding
    ) : RecyclerView.ViewHolder(binding.root), SettingsViewHolder {
        override fun bind(item: SettingItem) = with(binding) {
            if (item !is SettingItem.Title) return
            title.text = item.name
        }
    }

    class ToggleViewHolder(
        private val binding: ItemSettingsToggleBinding
    ) : RecyclerView.ViewHolder(binding.root), SettingsViewHolder {
        override fun bind(item: SettingItem) = with(binding) {
            if (item !is SettingItem.Toggle) return
            settingName.text = item.name
            settingDescription.text = item.description
            updateSwitcher(item.isEnabled)
        }

        private fun updateSwitcher(isEnable: Boolean) = with(binding) {
            switcher.isChecked = isEnable
        }
    }

    class VariantViewHolder(
        private val binding: ItemSettingsVariantBinding
    ) : RecyclerView.ViewHolder(binding.root), SettingsViewHolder {
        override fun bind(item: SettingItem) = with(binding) {
            if (item !is SettingItem.Variant) return
            settingName.text = item.name
            settingDescription.text = item.description
            updateSelector(item.variants)
        }

        private fun updateSelector(list: List<SettingVariant>) = with(binding) {
            val selectedVariant = list.firstOrNull { it.isSelected }
            this.selectedVariant.text = selectedVariant?.name
                ?: itemView.context.getString(R.string.variant_unchecked)
        }
    }

}