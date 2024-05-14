package com.github.mukiva.feature.dashboard.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.github.mukiva.feature.dashboard.domain.model.MinimalForecast

object MinimalForecastItemDiffCallback : DiffUtil.ItemCallback<MinimalForecast>() {
    override fun areItemsTheSame(oldItem: MinimalForecast, newItem: MinimalForecast): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: MinimalForecast, newItem: MinimalForecast): Boolean {
        return oldItem == newItem
    }
}
