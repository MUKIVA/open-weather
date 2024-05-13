package com.mukiva.feature.settings.ui.variantSelector

import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.settings.databinding.ItemSelectableItemBinding
import com.google.android.material.R as MaterialRes

class VariantViewHolder(
    private val binding: ItemSelectableItemBinding,
    private val onClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String, position: Int, isSelected: Boolean) = with(binding.variant) {
        text = item
        setSelection(isSelected)
        setOnClickListener { onClick(position) }
    }

    private fun setSelection(isSelected: Boolean) = with(binding.variant) {
        when (isSelected) {
            true -> getAttrColor(MaterialRes.attr.colorPrimary)
            false -> getAttrColor(MaterialRes.attr.colorOnSurface)
        }.let { colorRes ->
            setTextColor(colorRes)
            this.compoundDrawablesRelative.forEach {
                it?.setTint(colorRes)
            }
        }
    }

    private fun getAttrColor(@AttrRes res: Int): Int {
        val theme = itemView.context.theme
        val color = TypedValue()
        theme.resolveAttribute(res, color, true)
        return color.data
    }
}
