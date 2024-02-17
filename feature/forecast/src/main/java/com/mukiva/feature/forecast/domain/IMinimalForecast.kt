package com.mukiva.feature.forecast.domain

interface IMinimalForecast : IIndexedObject, IDatedObject {
    val dayAvgTempC: Double
    val dayAvgTempF: Double
    val nightAvgTempC: Double
    val nightAvgTempF: Double
    val conditionIconUrl: String
}