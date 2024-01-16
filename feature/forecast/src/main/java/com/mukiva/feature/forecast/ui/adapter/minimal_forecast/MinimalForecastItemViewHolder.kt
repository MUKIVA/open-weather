package com.mukiva.feature.forecast.ui.adapter.minimal_forecast

import androidx.recyclerview.widget.RecyclerView
import com.mukiva.feature.forecast.databinding.ItemMinForecastBinding
import com.mukiva.feature.forecast.domain.Forecast

class MinimalForecastItemViewHolder(
    private val bindings: ItemMinForecastBinding,
    private val onItemClick: () -> Unit
) : RecyclerView.ViewHolder(bindings.root) {

    fun bind(item: Forecast) = with(bindings) {
        root.setOnClickListener {
            onItemClick()
        }

    }

}