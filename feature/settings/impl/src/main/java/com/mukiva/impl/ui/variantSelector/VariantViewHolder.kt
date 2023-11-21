package com.mukiva.impl.ui.variantSelector

import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.settings_impl.databinding.ItemSelectableItemBinding
import com.mukiva.impl.domain.SettingVariant

class VariantViewHolder(
    private val binding: ItemSelectableItemBinding,
    private val onClick: (SettingVariant) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SettingVariant) = with(binding) {
        variant.text = item.name
        variant.isChecked = item.isSelected
        root.setOnClickListener { onClick(item) }
    }
}