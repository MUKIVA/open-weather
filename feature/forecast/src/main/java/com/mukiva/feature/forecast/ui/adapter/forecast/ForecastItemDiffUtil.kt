package com.mukiva.feature.forecast.ui.adapter.forecast

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.forecast.domain.ForecastItem
import java.util.LinkedList

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