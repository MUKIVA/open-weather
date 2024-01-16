package com.mukiva.feature.location_manager.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.location_manager.domain.model.Location

object LocationDiffUtil : DiffUtil.ItemCallback<Location>() {

    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return  oldItem == newItem
    }

}