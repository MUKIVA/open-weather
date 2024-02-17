package com.mukiva.feature.forecast.ui.adapter.forecast

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.forecast.domain.IForecastItem

object ForecastItemDiffUtil : DiffUtil.ItemCallback<IForecastItem>() {
    override fun areItemsTheSame(oldItem: IForecastItem, newItem: IForecastItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: IForecastItem, newItem: IForecastItem): Boolean {
        return oldItem == newItem
    }
}