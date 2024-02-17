package com.mukiva.feature.forecast.domain

import java.io.Serializable

interface IHourlyForecast : IIndexedObject, IDatedObject, Serializable {
    val groups: Collection<IForecastGroup<IForecastItem>>
}