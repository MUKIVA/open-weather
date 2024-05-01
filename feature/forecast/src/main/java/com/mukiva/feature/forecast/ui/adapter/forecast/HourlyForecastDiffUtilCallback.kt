package com.mukiva.feature.forecast.ui.adapter.forecast

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.forecast.presentation.HourlyForecast

class HourlyForecastDiffUtilCallback(
    private val oldList: List<HourlyForecast>,
    private val newList: List<HourlyForecast>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}