package com.mukiva.feature.location_manager.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mukiva.feature.location_manager.databinding.ItemLocationBinding
import com.mukiva.feature.location_manager.domain.model.Location

class LocationManagerSearchAdapter(
    private val onAddCallback: (location: Location) -> Unit
) : ListAdapter<Location, LocationViewHolder>(LocationDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LocationViewHolder(
            binding = ItemLocationBinding.inflate(inflater, parent, false),
            onAddCallback = onAddCallback
        )
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
