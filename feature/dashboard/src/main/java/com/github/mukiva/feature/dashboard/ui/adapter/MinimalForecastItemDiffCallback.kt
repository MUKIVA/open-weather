package com.github.mukiva.feature.dashboard.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.github.mukiva.feature.dashboard.domain.model.DayForecast

internal object MinimalForecastItemDiffCallback : DiffUtil.ItemCallback<DayForecast>() {
    override fun areItemsTheSame(oldItem: DayForecast, newItem: DayForecast): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DayForecast, newItem: DayForecast): Boolean {
        return oldItem == newItem
    }
}
