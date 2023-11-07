package com.mukiva.location_search.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.location_search.domain.LocationPoint

class LocationItemDiffUtil : DiffUtil.ItemCallback<LocationPoint>() {

    override fun areItemsTheSame(oldItem: LocationPoint, newItem: LocationPoint): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: LocationPoint, newItem: LocationPoint): Boolean {
        return  oldItem == newItem
    }

}