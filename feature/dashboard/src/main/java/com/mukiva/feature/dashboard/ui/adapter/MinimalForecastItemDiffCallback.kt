package com.mukiva.feature.dashboard.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.dashboard.domain.model.IMinimalForecast

object MinimalForecastItemDiffCallback : DiffUtil.ItemCallback<IMinimalForecast>() {
    override fun areItemsTheSame(oldItem: IMinimalForecast, newItem: IMinimalForecast): Boolean {
        return oldItem.index == newItem.index
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: IMinimalForecast, newItem: IMinimalForecast): Boolean {
        return oldItem == newItem
    }

}