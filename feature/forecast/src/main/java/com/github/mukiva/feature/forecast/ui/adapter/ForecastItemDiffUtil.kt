package com.github.mukiva.feature.forecast.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.github.mukiva.feature.forecast.domain.ForecastItem

object ForecastItemDiffUtil : DiffUtil.ItemCallback<ForecastItem>() {
    override fun areItemsTheSame(
        oldItem: ForecastItem,
        newItem: ForecastItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ForecastItem,
        newItem: ForecastItem
    ): Boolean {
        return oldItem == newItem
    }
}
