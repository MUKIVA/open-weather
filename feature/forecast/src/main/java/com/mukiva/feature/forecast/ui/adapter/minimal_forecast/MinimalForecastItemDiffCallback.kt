package com.mukiva.feature.forecast.ui.adapter.minimal_forecast

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.forecast.domain.IMinimalForecast

object MinimalForecastItemDiffCallback : DiffUtil.ItemCallback<IMinimalForecast>() {
    override fun areItemsTheSame(oldItem: IMinimalForecast, newItem: IMinimalForecast): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: IMinimalForecast, newItem: IMinimalForecast): Boolean {
        return oldItem == newItem
    }

}