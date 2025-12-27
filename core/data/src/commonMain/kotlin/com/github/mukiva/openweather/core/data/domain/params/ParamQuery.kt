package com.github.mukiva.openweather.core.data.domain.params

import kotlin.jvm.JvmInline

sealed interface ParamQuery {

    data class LatAndLong(
        val lat: Double,
        val lon: Double
    ) : ParamQuery {

        override fun toString(): String {
            return "$lat,$lon"
        }

    }

    @JvmInline
    value class City(val name: String) : ParamQuery

    @JvmInline
    value class SearchId(val id: Int) : ParamQuery

}