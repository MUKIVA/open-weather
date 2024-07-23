package com.github.mukiva.feature.settings.ui.variantSelector

import androidx.recyclerview.widget.DiffUtil

internal object VariantDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = true

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = false
}