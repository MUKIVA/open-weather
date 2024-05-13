package com.mukiva.feature.dashboard.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mukiva.open_weather.core.domain.weather.Temp
import com.mukiva.feature.dashboard.databinding.ItemMinForecastBinding
import com.mukiva.feature.dashboard.domain.model.MinimalForecast
import com.mukiva.openweather.ui.getTempString
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

class MinimalForecastItemViewHolder(
    private val bindings: ItemMinForecastBinding,
    private val onItemClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(bindings.root) {

    private val dayOfWeekFormatter = LocalDateTime.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
    }

    private val dayFormatter = LocalDateTime.Format {
        dayOfMonth(padding = Padding.NONE)
        char(' ')
        monthName(MonthNames.ENGLISH_FULL)
    }

    fun bind(item: MinimalForecast) = with(bindings) {
        updateCondition(item.conditionIconUrl)
        updateDate(item.date)
        updateTempInfo(
            dayTemp = item.maxTemp,
            nightTemp = item.minTemp,
        )

        root.setOnClickListener {
            onItemClick(item.id)
        }
    }

    private fun updateTempInfo(
        dayTemp: Temp,
        nightTemp: Temp,
    ) = with(bindings) {
        this.nightTemp.text = itemView.getTempString(nightTemp)
        this.dayTemp.text = itemView.getTempString(dayTemp)
    }

    private fun updateDate(date: LocalDateTime) = with(bindings) {
        this.date.text = dayFormatter.format(date)
        dayOfWeek.text = dayOfWeekFormatter.format(date)
    }

    private fun updateCondition(iconUrl: String) = with(bindings) {
        Glide.with(bindings.root)
            .load("https:$iconUrl")
            .centerCrop()
            .into(this.condition)
    }
}
