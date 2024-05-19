package com.github.mukiva.feature.dashboard.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.core.ui.getWeatherDrawable
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.databinding.ItemMinForecastBinding
import com.github.mukiva.feature.dashboard.domain.model.MinimalForecast
import com.github.mukiva.openweather.core.domain.weather.Temp
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
        dayOfWeek(localeDayOfWeak(itemView.context))
    }

    private val dayFormatter = LocalDateTime.Format {
        dayOfMonth(padding = Padding.NONE)
        char(' ')
        monthName(localeMonthNames(itemView.context))
    }

    fun bind(item: MinimalForecast) = with(bindings) {
        updateCondition(item.conditionIconCode, item.isDay)
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

    private fun updateCondition(iconCode: Int, isDay: Boolean) = with(bindings) {
        Glide.with(bindings.root)
            .load(itemView.context.getWeatherDrawable(iconCode, isDay))
            .centerCrop()
            .into(this.condition)
    }
}

internal fun localeDayOfWeak(context: Context): DayOfWeekNames = with(context) {
    return DayOfWeekNames(
        monday = getString(R.string.monday_full),
        tuesday = getString(R.string.tuesday_full),
        wednesday = getString(R.string.wednesday_full),
        thursday = getString(R.string.thursday_full),
        friday = getString(R.string.friday_full),
        saturday = getString(R.string.saturday_full),
        sunday = getString(R.string.sunday_full)
    )
}

internal fun localeMonthNames(context: Context): MonthNames = with(context) {
    return MonthNames(
        january = getString(R.string.january_full),
        february = getString(R.string.february_full),
        march = getString(R.string.march_full),
        april = getString(R.string.april_full),
        may = getString(R.string.may_full),
        june = getString(R.string.june_full),
        july = getString(R.string.july_full),
        august = getString(R.string.august_full),
        september = getString(R.string.september_full),
        october = getString(R.string.october_full),
        november = getString(R.string.november_full),
        december = getString(R.string.december_full)
    )
}
