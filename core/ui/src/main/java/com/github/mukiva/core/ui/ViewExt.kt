package com.github.mukiva.core.ui

import android.view.View
import androidx.core.view.isVisible
import com.github.mukiva.openweather.core.domain.settings.UnitsType
import com.github.mukiva.openweather.core.domain.weather.Distance
import com.github.mukiva.openweather.core.domain.weather.Precipitation
import com.github.mukiva.openweather.core.domain.weather.Pressure
import com.github.mukiva.openweather.core.domain.weather.Speed
import com.github.mukiva.openweather.core.domain.weather.Temp
import kotlin.math.roundToInt

fun View.gone() { isVisible = false }

fun View.visible() { isVisible = true }

fun View.getSpeedString(speed: Speed): String = with(speed) {
    return when (unitsType) {
        UnitsType.METRIC ->
            context.getString(R.string.template_kmh, value.roundToInt())
        UnitsType.IMPERIAL ->
            context.getString(R.string.template_mph, value.roundToInt())
    }
}

fun View.getTempString(temp: Temp): String = with(temp) {
    return when (unitsType) {
        UnitsType.METRIC ->
            context.getString(R.string.template_celsius, value.roundToInt())
        UnitsType.IMPERIAL ->
            context.getString(R.string.template_fahrenheit, value.roundToInt())
    }
}

fun View.getDistanceString(distance: Distance): String = with(distance) {
    return when (unitsType) {
        UnitsType.METRIC ->
            context.getString(R.string.template_km, value.roundToInt())
        UnitsType.IMPERIAL ->
            context.resources
                .getQuantityString(R.plurals.template_miles, value.roundToInt(), value.roundToInt())
    }
}

fun View.getPressureString(pressure: Pressure): String = with(pressure) {
    return when (unitsType) {
        UnitsType.METRIC ->
            context.getString(R.string.template_mb, value.roundToInt())
        UnitsType.IMPERIAL ->
            context.getString(R.string.template_mmhg, value.roundToInt())
    }
}

fun View.getPrecipitationString(precipitation: Precipitation): String = with(precipitation) {
    return when (unitsType) {
        UnitsType.METRIC ->
            context.getString(R.string.template_mm, value.roundToInt())
        UnitsType.IMPERIAL ->
            context.getString(R.string.template_in, value.roundToInt())
    }
}
