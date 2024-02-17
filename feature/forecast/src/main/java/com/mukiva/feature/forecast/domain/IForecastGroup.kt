package com.mukiva.feature.forecast.domain

import java.io.Serializable

interface IForecastGroup<T : IForecastItem> : Serializable {
    val items: Collection<T>
}