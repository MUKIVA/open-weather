package com.mukiva.feature.forecast.ui.adapter.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mukiva.feature.forecast.databinding.ItemHourDetailsBinding
import com.mukiva.feature.forecast.domain.ForecastItem

class ForecastItemAdapter : ListAdapter<ForecastItem, ForecastItemViewHolder>(ForecastItemDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ForecastItemViewHolder(
            binding = ItemHourDetailsBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ForecastItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
