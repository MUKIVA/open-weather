package com.mukiva.feature.forecast_impl

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.forecast_impl.presentation.DayForecastState

class ForecastDayDiffUtilCallback(
    private val oldList: List<DayForecastState>,
    private val newList: List<DayForecastState>
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