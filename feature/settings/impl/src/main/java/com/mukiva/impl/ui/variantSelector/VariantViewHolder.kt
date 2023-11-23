package com.mukiva.impl.ui.variantSelector

import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.settings_impl.R
import com.mukiva.feature.settings_impl.databinding.ItemSelectableItemBinding
import com.mukiva.impl.domain.SettingVariant
import org.w3c.dom.Attr

class VariantViewHolder(
    private val binding: ItemSelectableItemBinding,
    private val onClick: (SettingVariant) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SettingVariant) = with(binding.variant) {
        text = item.name
        setSelection(item.isSelected)
        setOnClickListener { onClick(item) }
    }

    private fun setSelection(isSelected: Boolean) = with(binding.variant) {
        setTextColor(when(isSelected) {
            true -> R.color.selection_text_color
            false -> R.color.default_text_color
        })
    }
}