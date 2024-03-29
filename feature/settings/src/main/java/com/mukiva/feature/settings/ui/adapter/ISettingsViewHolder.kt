package com.mukiva.feature.settings.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.settings.R
import com.mukiva.feature.settings.databinding.ItemSettingsGroupTitleBinding
import com.mukiva.feature.settings.databinding.ItemSettingsToggleBinding
import com.mukiva.feature.settings.databinding.ItemSettingsVariantBinding
import com.mukiva.feature.settings.domain.SettingItem
import com.mukiva.feature.settings.domain.SettingVariant

sealed interface ISettingsViewHolder {
    fun bind(item: SettingItem)

    class TitleViewHolder(
        private val binding: ItemSettingsGroupTitleBinding
    ) : RecyclerView.ViewHolder(binding.root), ISettingsViewHolder {
        override fun bind(item: SettingItem) = with(binding) {
            if (item !is SettingItem.Title) return
            title.text = with(SettingStringResolver) {
                itemView.context.resolveName(item.group)
            }
        }
    }

    class ToggleViewHolder(
        private val binding: ItemSettingsToggleBinding,
        private val onClick: (SettingItem.Toggle) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), ISettingsViewHolder {
        override fun bind(item: SettingItem) = with(binding) {
            if (item !is SettingItem.Toggle) return
            with(SettingStringResolver) {
                settingName.text = itemView.context.resolveName(item.group)
                settingDescription.text = itemView.context.resolveDescription(item.group)
            }
            updateSwitcher(item.isEnabled)

            root.setOnClickListener {
                onClick(item)
            }
        }

        private fun updateSwitcher(isEnable: Boolean) = with(binding) {
            switcher.isChecked = isEnable
        }
    }

    class VariantViewHolder(
        private val binding: ItemSettingsVariantBinding,
        private val onClick: (SettingItem.Variant) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), ISettingsViewHolder {
        override fun bind(item: SettingItem) = with(binding) {
            if (item !is SettingItem.Variant) return
            with(SettingStringResolver) {
                settingName.text = itemView.context.resolveName(item.group)
                settingDescription.text = itemView.context.resolveDescription(item.group)
            }
            updateSelector(item.variants)

            root.setOnClickListener {
                onClick(item)
            }
        }

        private fun updateSelector(list: List<SettingVariant>) = with(binding) {
            val selectedVariant = list.firstOrNull { it.isSelected }
            this.selectedVariant.text = selectedVariant?.name
                ?: itemView.context.getString(R.string.variant_unchecked)
        }
    }

}