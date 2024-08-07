package com.github.mukiva.feature.settings.ui.variantSelector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.github.mukiva.feature.settings.databinding.ItemSelectableItemBinding

internal class VariantAdapter(
    private val selectedPosition: () -> Int,
    private val onVariantSelect: (Int) -> Unit
) : ListAdapter<String, VariantViewHolder>(VariantDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VariantViewHolder(
            binding = ItemSelectableItemBinding.inflate(inflater, parent, false),
            onClick = onVariantSelect
        )
    }

    override fun onBindViewHolder(holder: VariantViewHolder, position: Int) {
        holder.bind(getItem(position), position, position == selectedPosition())
    }
}

