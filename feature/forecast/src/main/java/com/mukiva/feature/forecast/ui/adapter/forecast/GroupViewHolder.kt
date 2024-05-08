package com.mukiva.feature.forecast.ui.adapter.forecast

import androidx.recyclerview.widget.RecyclerView
import com.mukiva.core.ui.uiLazy
import com.mukiva.feature.forecast.R
import com.mukiva.feature.forecast.databinding.ItemForecastGroupTemplateBinding
import com.mukiva.feature.forecast.domain.ForecastItem
import com.mukiva.feature.forecast.domain.UnitsType
import com.mukiva.feature.forecast.presentation.ForecastGroup

class GroupViewHolder(
    private val bind: ItemForecastGroupTemplateBinding,
    private val unitsType: () -> UnitsType,
) : RecyclerView.ViewHolder(bind.root) {

    private val mItemAdapter by uiLazy {
        ForecastItemAdapter(unitsType)
    }

    fun bind(item: ForecastGroup) = with(bind) {
        updateItems(item.forecast)

        if (content.adapter == null)
            content.adapter = mItemAdapter

        updateTitle(item.forecastType)
    }

    private fun updateTitle(type: ForecastGroup.Type) = with(bind) {
        sectionName.text = when(type) {
            ForecastGroup.Type.TEMP -> itemView.context.getString(R.string.group_temp_title)
            ForecastGroup.Type.WIND -> itemView.context.getString(R.string.group_wind_title)
            ForecastGroup.Type.PRESSURE -> itemView.context.getString(R.string.group_pressure_title)
            ForecastGroup.Type.HUMIDITY -> itemView.context.getString(R.string.group_humidity_title)
        }
    }

    private fun updateItems(items: Collection<ForecastItem>) {
        mItemAdapter.submitList(items.toList())
    }

}