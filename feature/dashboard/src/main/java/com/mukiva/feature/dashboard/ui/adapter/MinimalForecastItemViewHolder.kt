package com.mukiva.feature.dashboard.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mukiva.feature.dashboard.databinding.ItemMinForecastBinding
import com.mukiva.feature.dashboard.domain.model.IMinimalForecast
import java.text.SimpleDateFormat
import java.util.Date

class MinimalForecastItemViewHolder(
    private val bindings: ItemMinForecastBinding,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(bindings.root) {

    private val dayFormatter = SimpleDateFormat(
        "d MMMM",
        itemView.context.resources.configuration.locales[0]
    )

    private val dayOfWeekFormatter = SimpleDateFormat(
        "EEEE",
        itemView.context.resources.configuration.locales[0]
    )

    fun bind(item: IMinimalForecast) = with(bindings) {

        updateCondition(item.conditionIconUrl)
        updateDate(item.date)
        updateTempInfo(item.dayAvgTempC, item.nightAvgTempC)

        root.setOnClickListener {
            onItemClick(item.index)
        }
    }

    private fun updateTempInfo(dayTemp: Double, nightTemp: Double) = with(bindings) {
        this.nightTemp.text = "${nightTemp.toInt()}"
        this.dayTemp.text = "${dayTemp.toInt()}"
    }

    private fun updateDate(date: Date) = with(bindings) {
        this.date.text = dayFormatter.format(date)
        dayOfWeek.text = dayOfWeekFormatter.format(date)
    }

    private fun updateCondition(iconUrl: String) = with (bindings) {
        Glide.with(bindings.root)
            .load("https:$iconUrl")
            .centerCrop()
            .into(this.condition)
    }

}