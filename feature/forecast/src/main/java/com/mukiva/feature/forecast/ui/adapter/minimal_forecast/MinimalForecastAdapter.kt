package com.mukiva.feature.forecast.ui.adapter.minimal_forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mukiva.feature.forecast.databinding.ItemMinForecastBinding
import com.mukiva.feature.forecast.domain.IMinimalForecast

class MinimalForecastAdapter(
    private val onItemClick: (Int) -> Unit
) : ListAdapter<IMinimalForecast, MinimalForecastItemViewHolder>(MinimalForecastItemDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MinimalForecastItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MinimalForecastItemViewHolder(
            bindings = ItemMinForecastBinding.inflate(inflater, parent, false),
            onItemClick = onItemClick
        )
    }

    override fun onBindViewHolder(holder: MinimalForecastItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}