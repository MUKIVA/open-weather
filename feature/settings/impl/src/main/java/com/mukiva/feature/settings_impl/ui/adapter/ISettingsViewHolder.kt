package com.mukiva.feature.settings_impl.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.settings_impl.R
import com.mukiva.feature.settings_impl.databinding.ItemSettingsGroupTitleBinding
import com.mukiva.feature.settings_impl.databinding.ItemSettingsToggleBinding
import com.mukiva.feature.settings_impl.databinding.ItemSettingsVariantBinding
import com.mukiva.feature.settings_impl.domain.SettingItem
import com.mukiva.feature.settings_impl.domain.SettingVariant
import com.mukiva.feature.settings_impl.domain.config.Group

object SettingStringResolver {
    fun Context.resolveName(group: Group): String {
        return when(group) {
            is Group.General.Theme -> getString(R.string.option_theme_name)
            is Group.General.UnitsType -> getString(R.string.option_units_type_name)
            is Group.General -> getString(R.string.setting_group_general)
        }
    }

    fun Context.resolveDescription(group: Group): String {
        return when(group) {
            is Group.General.Theme -> getString(R.string.option_theme_description)
            is Group.General.UnitsType -> getString(R.string.option_units_type_description)
            is Group.General -> ""
        }
    }

}

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