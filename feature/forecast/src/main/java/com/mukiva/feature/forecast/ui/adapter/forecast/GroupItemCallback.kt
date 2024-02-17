package com.mukiva.feature.forecast.ui.adapter.forecast

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.forecast.domain.IForecastGroup
import com.mukiva.feature.forecast.domain.IForecastItem

object GroupItemCallback : DiffUtil.ItemCallback<IForecastGroup<IForecastItem>>() {
    override fun areItemsTheSame(
        oldItem: IForecastGroup<IForecastItem>,
        newItem: IForecastGroup<IForecastItem>
    ): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: IForecastGroup<IForecastItem>,
        newItem: IForecastGroup<IForecastItem>
    ): Boolean {
        return oldItem == newItem
    }


}