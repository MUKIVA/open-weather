package com.github.mukiva.feature.locationmanager.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.github.mukiva.feature.locationmanager.databinding.ItemLocationBinding
import com.github.mukiva.feature.locationmanager.domain.model.Location

internal class LocationViewHolder(
    private val binding: ItemLocationBinding,
    private val onAddCallback: (location: Location) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Location) = with(binding) {
        updateCityName(item.cityName)
        updateRegionName(item.regionName)
        updateCountryName(item.countryName)

        root.setOnClickListener {
            onAddCallback(item)
        }
    }

    private fun updateCityName(name: String) = with(binding) {
        cityName.text = name
    }

    private fun updateRegionName(name: String) = with(binding) {
        region.text = name
    }

    private fun updateCountryName(name: String) = with(binding) {
        country.text = name
    }
}
