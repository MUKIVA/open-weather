package com.mukiva.location_search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.location_search.databinding.ItemLocationBinding
import com.mukiva.location_search.domain.LocationPoint

class SearchLocationAdapter(
    private val onAddCallback: (id: String) -> Unit
) : ListAdapter<LocationPoint, SearchLocationViewHolder>(
    LocationItemDiffUtil()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchLocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchLocationViewHolder(
            binding = ItemLocationBinding.inflate(inflater, parent, false),
            onAddCallback = onAddCallback
        )
    }

    override fun onBindViewHolder(holder: SearchLocationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}