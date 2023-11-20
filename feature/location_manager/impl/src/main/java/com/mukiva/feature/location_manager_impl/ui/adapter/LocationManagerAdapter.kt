package com.mukiva.feature.location_manager_impl.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mukiva.feature.location_manager_impl.databinding.ItemLocationBinding
import com.mukiva.feature.location_manager_impl.domain.model.Location

class LocationManagerAdapter(
    private val onAddCallback: (location: Location) -> Unit
) : ListAdapter<Location, LocationManagerViewHolder>(
    LocationManagerDiffUtil()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationManagerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LocationManagerViewHolder(
            binding = ItemLocationBinding.inflate(inflater, parent, false),
            onAddCallback = onAddCallback
        )
    }

    override fun onBindViewHolder(holder: LocationManagerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}