package com.mukiva.feature.dashboard.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.dashboard.domain.model.MinimalForecast

object MinimalForecastItemDiffCallback : DiffUtil.ItemCallback<MinimalForecast>() {
    override fun areItemsTheSame(oldItem: MinimalForecast, newItem: MinimalForecast): Boolean {
        return oldItem.index == newItem.index
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: MinimalForecast, newItem: MinimalForecast): Boolean {
        return oldItem == newItem
    }

}