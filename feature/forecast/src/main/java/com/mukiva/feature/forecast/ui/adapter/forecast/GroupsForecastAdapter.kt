package com.mukiva.feature.forecast.ui.adapter.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mukiva.feature.forecast.databinding.ItemForecastGroupTemplateBinding
import com.mukiva.feature.forecast.domain.UnitsType
import com.mukiva.feature.forecast.presentation.ForecastGroup

class GroupsForecastAdapter : ListAdapter<ForecastGroup, GroupViewHolder>(GroupItemCallback) {

    private var mUnitsType: UnitsType = UnitsType.METRIC

    fun updateUnitsType(unitsType: UnitsType) {
        mUnitsType = unitsType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GroupViewHolder(
            bind = ItemForecastGroupTemplateBinding.inflate(inflater, parent, false),
            unitsType = { mUnitsType }
        )
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}