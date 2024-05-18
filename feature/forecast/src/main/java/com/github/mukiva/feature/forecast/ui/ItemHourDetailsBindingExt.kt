package com.github.mukiva.feature.forecast.ui

import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.github.mukiva.core.ui.getDistanceString
import com.github.mukiva.core.ui.getPrecipitationString
import com.github.mukiva.core.ui.getPressureString
import com.github.mukiva.core.ui.getSpeedString
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.core.ui.getWeatherDrawable
import com.github.mukiva.feature.forecast.databinding.ItemHourDetailsBinding
import com.github.mukiva.openweather.core.domain.weather.Distance
import com.github.mukiva.openweather.core.domain.weather.Precipitation
import com.github.mukiva.openweather.core.domain.weather.Pressure
import com.github.mukiva.openweather.core.domain.weather.Speed
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.openweather.core.domain.weather.WindDirection
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char
import com.github.mukiva.core.ui.R as CoreUiRes

internal fun ItemHourDetailsBinding.updateFeelsLike(feelsLike: Temp) {
    feelsLikeValue.text = root.getTempString(feelsLike)
}

internal fun ItemHourDetailsBinding.updateVis(vis: Distance) {
    visValue.text = root.getDistanceString(vis)
}

internal fun ItemHourDetailsBinding.updatePrecipitation(precipitation: Precipitation) {
    precipitationValue.text = root.getPrecipitationString(precipitation)
}

internal fun ItemHourDetailsBinding.updateWindDirection(windDirection: WindDirection) {
    directionValue.text = windDirection.toString()
}

internal fun ItemHourDetailsBinding.updateTemp(temp: Temp) {
    tempValue.text = root.getTempString(temp)
}

internal fun ItemHourDetailsBinding.updatePressure(pressure: Pressure) {
    pressureValue.text = root.getPressureString(pressure)
}

internal fun ItemHourDetailsBinding.updateCloud(cloud: Int) {
    cloudValue.text =
        root.context.getString(CoreUiRes.string.template_percent, cloud)
}

internal fun ItemHourDetailsBinding.updateGust(gust: Speed) {
    gustValue.text = root.getSpeedString(gust)
}

internal fun ItemHourDetailsBinding.updateHumidity(humidity: Int) {
    humidityValue.text =
        root.context.getString(CoreUiRes.string.template_percent, humidity)
}
internal fun ItemHourDetailsBinding.updateTime(date: LocalDateTime) {
    time.text = mTimeFormatter.format(date)
}
internal fun ItemHourDetailsBinding.updateWeatherIcon(code: Int, isDay: Boolean) {
    Glide.with(root)
        .load(root.context.getWeatherDrawable(code, isDay))
        .into(weatherIcon)
}

internal val mTimeFormatter = LocalDateTime.Format {
    hour()
    char(':')
    minute()
}

internal fun ItemHourDetailsBinding.updateWindSpeed(windSpeed: Speed) {
    windSpeedValue.text = root.getSpeedString(windSpeed)
}

internal fun ItemHourDetailsBinding.updateDirectionIcon(drawable: Drawable) {
    windIcon.setImageDrawable(drawable)
}
