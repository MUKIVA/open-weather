package com.github.mukiva.openweather.core.data.domain.params

import com.github.mukiva.openweather.core.data.domain.common.IPType


sealed interface ParamQuery {

    data class LatAndLong(
        val lat: Double,
        val lon: Double
    ) : ParamQuery {

        override fun toString(): String {
            return "$lat,$lon"
        }

    }

    value class City(val name: String) : ParamQuery

    value class SearchId(val id: Int) : ParamQuery

}