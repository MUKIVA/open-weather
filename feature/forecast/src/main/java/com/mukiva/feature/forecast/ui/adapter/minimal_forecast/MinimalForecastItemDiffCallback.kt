package com.mukiva.feature.forecast.ui.adapter.minimal_forecast

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.forecast.domain.Forecast

object MinimalForecastItemDiffCallback : DiffUtil.ItemCallback<Forecast>() {
    override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
        return oldItem == newItem
    }

}