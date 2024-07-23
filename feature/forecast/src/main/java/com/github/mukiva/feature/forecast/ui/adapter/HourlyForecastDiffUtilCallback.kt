package com.github.mukiva.feature.forecast.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.github.mukiva.feature.forecast.presentation.HourlyForecast

internal object HourlyForecastDiffUtilCallback : DiffUtil.ItemCallback<HourlyForecast>() {

    override fun areItemsTheSame(
        oldItem: HourlyForecast,
        newItem: HourlyForecast
    ): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(
        oldItem: HourlyForecast,
        newItem: HourlyForecast
    ): Boolean {
        return oldItem == newItem
    }
}
