package com.github.mukiva.feature.forecast.ui.adapter

import android.graphics.drawable.RotateDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mukiva.feature.forecast.R
import com.github.mukiva.feature.forecast.databinding.ItemHourDetailsBinding
import com.github.mukiva.feature.forecast.domain.ForecastItem
import com.github.mukiva.feature.forecast.ui.updateCloud
import com.github.mukiva.feature.forecast.ui.updateDirectionIcon
import com.github.mukiva.feature.forecast.ui.updateFeelsLike
import com.github.mukiva.feature.forecast.ui.updateGust
import com.github.mukiva.feature.forecast.ui.updateHumidity
import com.github.mukiva.feature.forecast.ui.updatePrecipitation
import com.github.mukiva.feature.forecast.ui.updatePressure
import com.github.mukiva.feature.forecast.ui.updateTemp
import com.github.mukiva.feature.forecast.ui.updateTime
import com.github.mukiva.feature.forecast.ui.updateVis
import com.github.mukiva.feature.forecast.ui.updateWeatherIcon
import com.github.mukiva.feature.forecast.ui.updateWindDirection
import com.github.mukiva.feature.forecast.ui.updateWindSpeed

internal class ForecastItemViewHolder(
    private val binding: ItemHourDetailsBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ForecastItem) = with(binding) {
        updateTemp(item.temp)
        updateFeelsLike(item.feelsLike)
        updateCloud(item.cloud)
        updateWindSpeed(item.windSpeed)
        updateDegree(item.windDegree)
        updateWindDirection(item.windDirection)
        updatePressure(item.pressure)
        updatePrecipitation(item.precipitation)
        updateVis(item.vis)
        updateGust(item.gust)
        updateHumidity(item.humidity)
        updateTime(item.dateTime)
        updateWeatherIcon(item.weatherIconCode, item.isDay)
    }

    private fun updateDegree(degree: Int) = with(binding) {
        val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_dir)
        val rotatedDrawable = RotateDrawable().apply {
            fromDegrees = 0f
            toDegrees = degree.toFloat()
            level =
                ROTATE_DRAWABLE_MAX_LEVEL
            setDrawable(drawable)
        }

        updateDirectionIcon(rotatedDrawable)
    }

    companion object {
        private const val ROTATE_DRAWABLE_MAX_LEVEL = 10000
    }
}
