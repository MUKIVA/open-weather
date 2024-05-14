package com.github.mukiva.feature.locationmanager.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.github.mukiva.feature.locationmanager.domain.model.Location

object LocationDiffUtil : DiffUtil.ItemCallback<Location>() {

    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }
}
