package com.mukiva.location_search.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.location_search.domain.model.Location

class LocationItemDiffUtil : DiffUtil.ItemCallback<Location>() {

    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return  oldItem == newItem
    }

}