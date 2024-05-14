package com.github.mukiva.feature.forecast.ui.adapter

import android.graphics.drawable.RotateDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mukiva.openweather.core.domain.weather.Distance
import com.github.mukiva.openweather.core.domain.weather.Precipitation
import com.github.mukiva.openweather.core.domain.weather.Pressure
import com.github.mukiva.openweather.core.domain.weather.Speed
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.openweather.core.domain.weather.WindDirection
import com.github.mukiva.feature.forecast.domain.ForecastItem
import com.github.mukiva.core.ui.getDistanceString
import com.github.mukiva.core.ui.getPrecipitationString
import com.github.mukiva.core.ui.getPressureString
import com.github.mukiva.core.ui.getSpeedString
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.feature.forecast.R
import com.github.mukiva.feature.forecast.databinding.ItemHourDetailsBinding
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char
import com.github.mukiva.core.ui.R as CoreUiRes

class ForecastItemViewHolder(
    private val binding: ItemHourDetailsBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val mTimeFormatter = LocalDateTime.Format {
        hour()
        char(':')
        minute()
    }

    fun bind(item: ForecastItem) {
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
        updateWeatherIcon(item.weatherIconUrl)
    }

    private fun updateTemp(temp: Temp) = with(binding.firstDetails) {
        tempValue.text = itemView.getTempString(temp)
    }
    private fun updateFeelsLike(feelsLike: Temp) = with(binding.firstDetails) {
        feelsLikeValue.text = itemView.getTempString(feelsLike)
    }
    private fun updateCloud(cloud: Int) = with(binding.firstDetails) {
        cloudValue.text =
            itemView.context.getString(CoreUiRes.string.template_percent, cloud)
    }
    private fun updateWindSpeed(windSpeed: Speed) = with(binding.firstDetails) {
        windSpeedValue.text = itemView.getSpeedString(windSpeed)
    }
    private fun updateDegree(degree: Int) = with(binding.firstDetails) {
        degreeValue.text =
            itemView.context.getString(CoreUiRes.string.template_degree, degree)
        val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_dir)
        val rotatedDrawable = RotateDrawable().apply {
            fromDegrees = 0f
            toDegrees = degree.toFloat()
            level = ROTATE_DRAWABLE_MAX_LEVEL
            setDrawable(drawable)
        }

        binding.windIcon.setImageDrawable(rotatedDrawable)
    }

    private fun updateWindDirection(windDirection: WindDirection) = with(binding.firstDetails) {
        directionValue.text = windDirection.toString()
    }
    private fun updatePressure(pressure: Pressure) = with(binding.secondDetails) {
        pressureValue.text = itemView.getPressureString(pressure)
    }
    private fun updatePrecipitation(precipitation: Precipitation) = with(binding.secondDetails) {
        precipitationValue.text = itemView.getPrecipitationString(precipitation)
    }
    private fun updateVis(vis: Distance) = with(binding.secondDetails) {
        visValue.text = itemView.getDistanceString(vis)
    }
    private fun updateGust(gust: Speed) = with(binding.secondDetails) {
        gustValue.text = itemView.getSpeedString(gust)
    }
    private fun updateHumidity(humidity: Int) = with(binding.secondDetails) {
        humidityValue.text =
            itemView.context.getString(CoreUiRes.string.template_percent, humidity)
    }
    private fun updateTime(date: LocalDateTime) = with(binding) {
        time.text = mTimeFormatter.format(date)
    }
    private fun updateWeatherIcon(url: String) = with(binding) {
        Glide.with(itemView)
            .load("https:$url")
            .into(weatherIcon)
    }

    companion object {
        private const val ROTATE_DRAWABLE_MAX_LEVEL = 10000
    }
}
