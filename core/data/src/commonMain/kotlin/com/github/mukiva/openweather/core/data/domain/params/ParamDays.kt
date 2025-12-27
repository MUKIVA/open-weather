package com.github.mukiva.openweather.core.data.domain.params

import kotlin.jvm.JvmInline

@JvmInline
value class ParamDays(val count: Int) {

    init { assertInAvailableRange(count) }

    private fun assertInAvailableRange(count: Int) {
        if (count !in 1..14) {
            error("Param days is out of bounds")
        }
    }

}