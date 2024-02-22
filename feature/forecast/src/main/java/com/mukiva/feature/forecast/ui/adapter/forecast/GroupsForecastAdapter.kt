package com.mukiva.feature.forecast.ui.adapter.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mukiva.feature.forecast.databinding.ItemForecastGroupTemplateBinding
import com.mukiva.feature.forecast.domain.IForecastGroup
import com.mukiva.feature.forecast.domain.IForecastItem
import com.mukiva.feature.forecast.domain.UnitsType

class GroupsForecastAdapter(
    private val unitsType: UnitsType
) : ListAdapter<IForecastGroup<IForecastItem>, GroupViewHolder>(GroupItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GroupViewHolder(
            bind = ItemForecastGroupTemplateBinding.inflate(inflater, parent, false),
            unitsType = unitsType
        )
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}