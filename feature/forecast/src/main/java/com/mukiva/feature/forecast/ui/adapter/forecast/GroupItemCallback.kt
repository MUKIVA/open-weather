package com.mukiva.feature.forecast.ui.adapter.forecast

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.forecast.presentation.ForecastGroup

object GroupItemCallback : DiffUtil.ItemCallback<ForecastGroup>() {
    override fun areItemsTheSame(
        oldItem: ForecastGroup,
        newItem: ForecastGroup
    ): Boolean {
        return oldItem.forecastType == newItem.forecastType
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: ForecastGroup,
        newItem: ForecastGroup
    ): Boolean {
        return oldItem == newItem
    }


}