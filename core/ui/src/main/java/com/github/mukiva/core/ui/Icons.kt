package com.github.mukiva.core.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources

fun Context.getWeatherDrawable(code: Int, isDay: Boolean): Drawable? {
    return AppCompatResources.getDrawable(this, getWeatherIconRes(code, isDay))
}

fun Context.getWeatherRes(code: Int, isDay: Boolean): Int {
    return getWeatherIconRes(code, isDay)
}

fun Context.getWeatherDescription(code: Int): String {
    return getString(getWeatherDescriptionRes(code))
}

private fun getWeatherIconRes(code: Int, isDay: Boolean): Int {
    return when (code) {
        1000 -> if (isDay) R.drawable.ic_sunny_day else R.drawable.ic_clear_night
        1003 -> if (isDay) R.drawable.ic_partly_cloudy_day else R.drawable.ic_partly_cloudy_night
        1006 -> if (isDay) R.drawable.ic_cloudy_day else R.drawable.ic_cloudy_nght
        1009 -> if (isDay) R.drawable.ic_overcast_day else R.drawable.ic_overcast_night
        1030 -> if (isDay) R.drawable.ic_mist_day else R.drawable.ic_mist_night
        1063 -> if (isDay) R.drawable.ic_patchy_rain_nearby_day else R.drawable.ic_patchy_rain_nearby_night
        1066 -> if (isDay) R.drawable.ic_patchy_snow_nearby_day else R.drawable.ic_patchy_snow_nearby_night
        1069 -> if (isDay) R.drawable.ic_patchy_sleet_nearby_day else R.drawable.ic_patchy_sleet_nearby_night
        1072 -> if (isDay) R.drawable.ic_patchy_freezing_drizzle_nearby_day else R.drawable.ic_patchy_freezing_drizzle_nearby_night
        1087 -> if (isDay) R.drawable.ic_thundery_outbreaks_in_nearby_day else R.drawable.ic_thundery_outbreaks_in_nearby_night
        1114 -> if (isDay) R.drawable.ic_blowing_snow_day else R.drawable.ic_blowing_snow_night
        1117 -> if (isDay) R.drawable.ic_blizzard_day else R.drawable.ic_blizzard_night
        1135 -> if (isDay) R.drawable.ic_fog_day else R.drawable.ic_fog_night
        1147 -> if (isDay) R.drawable.ic_freezing_fog_day else R.drawable.ic_freezing_fog_night
        1150 -> if (isDay) R.drawable.ic_patchy_light_drizzle_day else R.drawable.ic_patchy_light_drizzle_night
        1153 -> if (isDay) R.drawable.ic_light_drizzle_day else R.drawable.ic_light_drizzle_night
        1168 -> if (isDay) R.drawable.ic_freezing_drizzle_day else R.drawable.ic_freezing_drizzle_night
        1171 -> if (isDay) R.drawable.ic_heavy_freezing_drizzle_day else R.drawable.ic_heavy_freezing_drizzle_night
        1180 -> if (isDay) R.drawable.ic_patchy_light_rain_day else R.drawable.ic_patchy_light_rain_night
        1183 -> if (isDay) R.drawable.ic_light_rain_day else R.drawable.ic_light_rain_night
        1186 -> if (isDay) R.drawable.ic_moderate_rain_at_times_day else R.drawable.ic_moderate_rain_at_times_night
        1189 -> if (isDay) R.drawable.ic_moderate_rain_day else R.drawable.ic_moderate_rain_night
        1192 -> if (isDay) R.drawable.ic_heavy_rain_at_times_day else R.drawable.ic_heavy_rain_at_times_night
        1195 -> if (isDay) R.drawable.ic_heavy_rain_day else R.drawable.ic_heavy_rain_night
        1198 -> if (isDay) R.drawable.ic_light_freezing_rain_day else R.drawable.ic_light_freezing_rain_night
        1201 -> if (isDay) R.drawable.ic_moderate_or_heavy_freezing_rain_day else R.drawable.ic_moderate_or_heavy_freezing_rain_night
        1204 -> if (isDay) R.drawable.ic_light_sleet_day else R.drawable.ic_light_sleet_night
        1207 -> if (isDay) R.drawable.ic_moderate_or_heavy_sleet_day else R.drawable.ic_moderate_or_heavy_sleet_night
        1210 -> if (isDay) R.drawable.ic_patchy_light_snow_day else R.drawable.ic_patchy_light_snow_night
        1213 -> if (isDay) R.drawable.ic_light_snow_day else R.drawable.ic_light_snow_night
        1216 -> if (isDay) R.drawable.ic_patchy_moderate_snow_day else R.drawable.ic_patchy_moderate_snow_night
        1219 -> if (isDay) R.drawable.ic_moderate_snow_day else R.drawable.ic_moderate_snow_night
        1222 -> if (isDay) R.drawable.ic_patchy_heavy_snow_day else R.drawable.ic_patchy_heavy_snow_night
        1225 -> if (isDay) R.drawable.ic_heavy_snow_day else R.drawable.ic_heavy_snow_night
        1237 -> if (isDay) R.drawable.ic_ice_pellets_day else R.drawable.ic_ice_pellets_night
        1240 -> if (isDay) R.drawable.ic_light_rain_shower_day else R.drawable.ic_light_rain_shower_night
        1243 -> if (isDay) R.drawable.ic_moderate_or_heavy_rain_shower_day else R.drawable.ic_moderate_or_heavy_rain_shower_night
        1246 -> if (isDay) R.drawable.ic_torrential_rain_shower_day else R.drawable.ic_torrential_rain_shower_night
        1249 -> if (isDay) R.drawable.ic_light_sleet_showers_day else R.drawable.ic_light_sleet_showers_night
        1252 -> if (isDay) R.drawable.ic_moderate_or_heavy_sleet_showers_day else R.drawable.ic_moderate_or_heavy_sleet_showers_night
        1255 -> if (isDay) R.drawable.ic_light_snow_showers_day else R.drawable.ic_light_snow_showers_night
        1258 -> if (isDay) R.drawable.ic_moderate_or_heavy_snow_showers_day else R.drawable.ic_moderate_or_heavy_snow_showers_night
        1261 -> if (isDay) R.drawable.ic_light_showers_of_ice_pellets_day else R.drawable.ic_light_showers_of_ice_pellets_night
        1264 -> if (isDay) R.drawable.ic_moderate_or_heavy_showers_of_ice_pellets_day else R.drawable.ic_moderate_or_heavy_showers_of_ice_pellets_night
        1273 -> if (isDay) R.drawable.ic_patchy_light_rain_in_area_with_thunder_day else R.drawable.ic_patchy_light_rain_in_area_with_thunder_night
        1276 -> if (isDay) R.drawable.ic_moderate_or_heavy_rain_in_area_with_thunder_day else R.drawable.ic_moderate_or_heavy_rain_in_area_with_thunder_night
        1279 -> if (isDay) R.drawable.ic_patchy_light_snow_in_area_with_thunder_day else R.drawable.ic_patchy_light_snow_in_area_with_thunder_night
        1282 -> if (isDay) R.drawable.ic_moderate_or_heavy_snow_in_area_with_thunder_day else R.drawable.ic_moderate_or_heavy_snow_in_area_with_thunder_night
        else -> error("Icon not found")
    }
}

private fun getWeatherDescriptionRes(code: Int): Int {
    return when(code) {
        1000 -> R.string.clear
        1003 -> R.string.partly_cloudy
        1006 -> R.string.cloudy
        1009 -> R.string.overcast
        1030 -> R.string.mist
        1063 -> R.string.patchy_rain_nearby
        1066 -> R.string.patchy_snow_nearby
        1069 -> R.string.patchy_sleet_nearby
        1072 -> R.string.patchy_freezing_drizzle_nearby
        1087 -> R.string.thundery_outbreaks_in_nearby
        1114 -> R.string.blowing_snow
        1117 -> R.string.blizzard
        1135 -> R.string.fog
        1147 -> R.string.freezing_fog
        1150 -> R.string.patchy_light_drizzle
        1153 -> R.string.light_drizzle
        1168 -> R.string.freezing_drizzle
        1171 -> R.string.heavy_freezing_drizzle
        1180 -> R.string.patchy_light_rain
        1183 -> R.string.light_rain
        1186 -> R.string.moderate_rain_at_times
        1189 -> R.string.moderate_rain
        1192 -> R.string.heavy_rain_at_times
        1195 -> R.string.heavy_rain
        1198 -> R.string.light_freezing_rain
        1201 -> R.string.moderate_or_heavy_freezing_rain
        1204 -> R.string.light_sleet
        1207 -> R.string.moderate_or_heavy_sleet
        1210 -> R.string.patchy_light_snow
        1213 -> R.string.light_snow
        1216 -> R.string.patchy_moderate_snow
        1219 -> R.string.moderate_snow
        1222 -> R.string.patchy_heavy_snow
        1225 -> R.string.heavy_snow
        1237 -> R.string.ice_pellets
        1240 -> R.string.light_rain_shower
        1243 -> R.string.moderate_or_heavy_rain_shower
        1246 -> R.string.torrential_rain_shower
        1249 -> R.string.light_sleet_showers
        1252 -> R.string.moderate_or_heavy_sleet_showers
        1255 -> R.string.light_snow_showers
        1258 -> R.string.moderate_or_heavy_snow_showers
        1261 -> R.string.light_showers_of_ice_pellets
        1264 -> R.string.moderate_or_heavy_showers_of_ice_pellets
        1273 -> R.string.patchy_light_rain_in_area_with_thunder
        1276 -> R.string.moderate_or_heavy_rain_in_area_with_thunder
        1279 -> R.string.patchy_light_snow_in_area_with_thunder
        1282 -> R.string.moderate_or_heavy_snow_in_area_with_thunder
        else -> error("Not found description")
    }
}

