package com.mukiva.feature.dashboard.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mukiva.feature.dashboard.R
import com.mukiva.feature.dashboard.databinding.ItemMinForecastBinding
import com.mukiva.feature.dashboard.domain.model.IMinimalForecast
import com.mukiva.feature.dashboard.domain.model.UnitsType
import com.mukiva.feature.dashboard.presentation.IDashboardState
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

class MinimalForecastItemViewHolder(
    private val bindings: ItemMinForecastBinding,
    private val onItemClick: (Int) -> Unit,
    private val unitsTypeProvider: IDashboardState
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
        updateTempInfo(
            item.dayAvgTempC,
            item.dayAvgTempF,
            item.nightAvgTempC,
            item.nightAvgTempF,
            unitsTypeProvider.unitsType
        )

        root.setOnClickListener {
            onItemClick(item.index)
        }
    }

    private fun updateTempInfo(
        dayTempC: Double,
        dayTempF: Double,
        nightTempC: Double,
        nightTempF: Double,
        unitsType: UnitsType
    ) = with(bindings) {
        val (strRes, night, day) = when(unitsType) {
            UnitsType.METRIC -> Triple(
                R.string.template_celsius_main_card,
                nightTempC.roundToInt(),
                dayTempC.roundToInt()
            )
            UnitsType.IMPERIAL -> Triple(
                R.string.template_fahrenheit_main_card,
                nightTempF.roundToInt(),
                dayTempF.roundToInt()
            )
        }

        this.nightTemp.text = itemView.context.getString(strRes, night)
        this.dayTemp.text = itemView.context.getString(strRes, day)
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