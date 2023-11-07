package com.mukiva.location_search.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.mukiva.location_search.databinding.ItemLocationBinding
import com.mukiva.location_search.domain.LocationPoint

class SearchLocationViewHolder(
    private val binding: ItemLocationBinding,
    private val onAddCallback: (id: String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: LocationPoint) = with(binding) {
        updateCityName(item.cityName)
        updateRegionName(item.regionName)
        updateCountryName(item.countryName)

        binding.root.setOnClickListener {
            onAddCallback(item.cityName)
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