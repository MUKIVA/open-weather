package com.mukiva.feature.settings_impl.ui.variantSelector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mukiva.feature.settings_impl.databinding.ItemSelectableItemBinding
import com.mukiva.feature.settings_impl.domain.SettingVariant

class VariantListAdapter(
    private val onVariantSelect: (SettingVariant) -> Unit
) : ListAdapter<SettingVariant, VariantViewHolder>(VariantDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VariantViewHolder(
            binding = ItemSelectableItemBinding.inflate(inflater, parent, false),
            onClick = onVariantSelect
        )
    }

    override fun onBindViewHolder(holder: VariantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}