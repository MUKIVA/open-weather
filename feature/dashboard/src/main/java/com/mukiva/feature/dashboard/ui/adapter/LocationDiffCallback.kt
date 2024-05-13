package com.mukiva.feature.dashboard.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.dashboard.domain.model.Location

object LocationDiffCallback : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }
}
