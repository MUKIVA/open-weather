package com.mukiva.feature.forecast.ui.adapter.forecast

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.forecast.presentation.HourlyForecast

object HourlyForecastDiffUtilCallback : DiffUtil.ItemCallback<HourlyForecast.Content>() {

    override fun areItemsTheSame(
        oldItem: HourlyForecast.Content,
        newItem: HourlyForecast.Content
    ): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(
        oldItem: HourlyForecast.Content,
        newItem: HourlyForecast.Content
    ): Boolean {
        return oldItem == newItem
    }
}