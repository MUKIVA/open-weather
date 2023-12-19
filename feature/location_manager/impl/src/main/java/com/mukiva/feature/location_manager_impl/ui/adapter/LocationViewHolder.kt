package com.mukiva.feature.location_manager_impl.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.location_manager_impl.databinding.ItemLocationBinding
import com.mukiva.feature.location_manager_impl.domain.model.Location

class LocationViewHolder(
    private val binding: ItemLocationBinding,
    private val onAddCallback: (location: Location) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Location) = with(binding) {
        updateCityName(item.cityName)
        updateRegionName(item.regionName)
        updateCountryName(item.countryName)

        binding.root.setOnClickListener {
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