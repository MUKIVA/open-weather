package com.mukiva.feature.forecast.ui.adapter.forecast

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.feature.forecast.domain.IHourlyForecast

class HourlyForecastDiffUtilCallback(
    private val oldList: Collection<IHourlyForecast>,
    private val newList: Collection<IHourlyForecast>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.elementAt(oldItemPosition).id == newList.elementAt(newItemPosition).id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.elementAt(oldItemPosition) == newList.elementAt(newItemPosition)
    }
}