package com.mukiva.feature.forecast.domain

import java.io.Serializable

sealed interface IForecastItem : Serializable {
    interface IHourlyHumidity : IDatedObject, IForecastItem {
        val humidity: Int
    }
    interface IHourlyPressure : IDatedObject, IForecastItem {
        val pressureMb: Double
        val pressureIn: Double
    }
    interface IHourlyTemp : IDatedObject, IForecastItem {
        val tempC: Double
        val tempF: Double
        val feelsLikeC: Double
        val feelsLikeF: Double
        val cloud: Double
        val iconUrl: String
    }
    interface IHourlyWind : IDatedObject, IForecastItem {
        val windMph: Double
        val windKph: Double
        val windDegree: Int
        val windDirection: WindDirection
    }

}