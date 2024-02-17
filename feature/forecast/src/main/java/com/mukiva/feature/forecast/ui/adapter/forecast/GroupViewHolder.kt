package com.mukiva.feature.forecast.ui.adapter.forecast

import androidx.recyclerview.widget.RecyclerView
import com.mukiva.core.ui.uiLazy
import com.mukiva.feature.forecast.R
import com.mukiva.feature.forecast.databinding.ItemForecastGroupTemplateBinding
import com.mukiva.feature.forecast.domain.IForecastGroup
import com.mukiva.feature.forecast.domain.IForecastItem
import com.mukiva.feature.forecast.presentation.ForecastGroup
import com.mukiva.openweather.ui.gone

class GroupViewHolder(
    private val bind: ItemForecastGroupTemplateBinding
) : RecyclerView.ViewHolder(bind.root) {

    private val mItemAdapter by uiLazy { ForecastItemAdapter() }

    fun bind(item: IForecastGroup<IForecastItem>) = with(bind) {
        updateItems(item.items)

        if (content.adapter == null)
            content.adapter = mItemAdapter

        if (item is ForecastGroup)
            updateTitle(item.type)
        else
            hideSectionName()
    }

    private fun hideSectionName() = bind.sectionName.gone()

    private fun updateTitle(type: ForecastGroup.GroupType) = with(bind) {
        sectionName.text = when(type) {
            ForecastGroup.GroupType.TEMP -> itemView.context.getString(R.string.group_temp_title)
            ForecastGroup.GroupType.WIND -> itemView.context.getString(R.string.group_wind_title)
            ForecastGroup.GroupType.PRESSURE -> itemView.context.getString(R.string.group_pressure_title)
            ForecastGroup.GroupType.HUMIDITY -> itemView.context.getString(R.string.group_humidity_title)
        }
    }

    private fun updateItems(items: Collection<IForecastItem>) {
        mItemAdapter.submitList(items.toList())
    }

}