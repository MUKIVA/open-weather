package com.github.mukiva.feature.settings.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.github.mukiva.feature.settings.databinding.ItemSettingsGroupTitleBinding
import com.github.mukiva.feature.settings.databinding.ItemSettingsToggleBinding
import com.github.mukiva.feature.settings.databinding.ItemSettingsVariantBinding
import com.github.mukiva.feature.settings.domain.SettingItem
import kotlin.reflect.KClass

sealed interface ISettingsViewHolder {
    fun bind(item: SettingItem)

    class TitleViewHolder(
        private val binding: ItemSettingsGroupTitleBinding
    ) : RecyclerView.ViewHolder(binding.root), ISettingsViewHolder {
        override fun bind(item: SettingItem) = with(binding) {
            if (item !is SettingItem.Title) return
            title.text = itemView.context.resolveName(item)
        }
    }

    class ToggleViewHolder(
        private val binding: ItemSettingsToggleBinding,
        private val onClick: (KClass<*>, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), ISettingsViewHolder {
        override fun bind(item: SettingItem) = with(binding) {
            if (item !is SettingItem.Toggle) return

            updateName(item)
            updateDescription(item)
            updateSwitcher(item.isEnabled)

            root.setOnClickListener {
                switcher.isChecked = !switcher.isChecked
            }
            switcher.setOnCheckedChangeListener { buttonView, isChecked ->
                onClick(item.key, isChecked)
            }
        }

        private fun updateName(item: SettingItem.Toggle) = with(binding) {
            settingName.text = itemView.context.resolveName(item)
        }

        private fun updateDescription(item: SettingItem.Toggle) = with(binding) {
            settingDescription.text = itemView.context.resolveDescription(item)
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

            updateName(item)
            updateDescription(item)
            updateSelector(item.selectedVariant)

            root.setOnClickListener {
                onClick(item)
            }
        }

        private fun updateName(item: SettingItem.Variant) = with(binding) {
            settingName.text = itemView.context.resolveName(item)
        }

        private fun updateDescription(item: SettingItem.Variant) = with(binding) {
            settingDescription.text = itemView.context.resolveDescription(item)
        }

        private fun updateSelector(selectedVariant: Enum<*>) = with(binding) {
            this.selectedVariant.text = itemView.context.asLocalVariant(selectedVariant)
        }
    }
}
