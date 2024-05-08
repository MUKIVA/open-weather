package com.mukiva.feature.dashboard.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mukiva.core.ui.R as CoreUiRes
import com.mukiva.feature.dashboard.databinding.ItemMinForecastBinding
import com.mukiva.feature.dashboard.domain.model.MinimalForecast
import com.mukiva.feature.dashboard.domain.model.UnitsType
import com.mukiva.feature.dashboard.presentation.IDashboardState
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

class MinimalForecastItemViewHolder(
    private val bindings: ItemMinForecastBinding,
    private val onItemClick: (Int) -> Unit,
    private val unitsTypeProvider: IDashboardState
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
            item.maxTempC,
            item.maxTempF,
            item.avgTempC,
            item.avgTempF,
            unitsTypeProvider.unitsType
        )

        root.setOnClickListener {
            onItemClick(item.id)
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
                CoreUiRes.string.template_celsius,
                nightTempC.roundToInt(),
                dayTempC.roundToInt()
            )
            UnitsType.IMPERIAL -> Triple(
                CoreUiRes.string.template_fahrenheit,
                nightTempF.roundToInt(),
                dayTempF.roundToInt()
            )
        }

        this.nightTemp.text = itemView.context.getString(strRes, night)
        this.dayTemp.text = itemView.context.getString(strRes, day)
    }

    private fun updateDate(date: LocalDateTime) = with(bindings) {
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