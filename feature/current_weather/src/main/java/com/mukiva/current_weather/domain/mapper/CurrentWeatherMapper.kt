package com.mukiva.current_weather.domain.mapper

import com.mukiva.current_weather.data.CurrentJson
import com.mukiva.current_weather.domain.model.Condition
import com.mukiva.current_weather.domain.model.CurrentWeather
import com.mukiva.current_weather.domain.model.WindDirection
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CurrentWeatherMapper @Inject constructor(
    private val conditionMapper: ConditionMapper
) {
    private val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

    fun CurrentJson.mapToDomain(): CurrentWeather {
        return CurrentWeather(
            lastUpdatedEpoch = lastUpdatedEpoch ?: 0,
            lastUpdated = simpleDateFormat.parse(lastUpdated ?: "")
                ?: throw Exception("Can't parse lastUpdated"),
            tempC = tempC?.toFloat() ?: 0.0f,
            tempF = tempF?.toFloat() ?: 0.0f,
            isDay = isDay != 0,
            condition = with(conditionMapper) {
                condition?.mapToDomain() ?: Condition.default()
                                              },
            windMph = windMph?.toFloat() ?: 0.0f,
            windKph = windKph?.toFloat() ?: 0.0f,
            windDegree = windDegree ?: 0,
            windDir = windDir?.let { WindDirection.valueOf(it) } ?: WindDirection.UNKNOWN,
            pressureMb = pressureMb?.toFloat() ?: 0.0f,
            pressureIn = pressureIn?.toFloat() ?: 0.0f,
            precipMm = precipMm?.toFloat() ?: 0.0f,
            precipIn = precipIn?.toFloat() ?: 0.0f,
            humidity = humidity ?: 0,
            cloud = cloud ?: 0,
            feelsLikeC = feelslikeC?.toFloat() ?: 0.0f,
            feelsLikeF = feelslikeF?.toFloat() ?: 0.0f,
            visKm = visKm?.toFloat() ?: 0.0f,
            visMiles = visKm?.toFloat() ?: 0.0f,
            uv = uv?.toFloat() ?: 0.0f,
            gustMph = gustMph?.toFloat() ?: 0.0f,
            gustKph = gustKph?.toFloat() ?: 0.0f
        )
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd HH:mm"
    }

}