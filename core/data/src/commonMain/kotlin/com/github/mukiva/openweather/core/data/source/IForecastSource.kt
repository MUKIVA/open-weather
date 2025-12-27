package com.github.mukiva.openweather.core.data.source

import com.github.mukiva.openweather.core.data.domain.params.ParamAlerts
import com.github.mukiva.openweather.core.data.domain.params.ParamAqi
import com.github.mukiva.openweather.core.data.domain.params.ParamDays
import com.github.mukiva.openweather.core.data.domain.params.ParamLang
import com.github.mukiva.openweather.core.data.domain.params.ParamQuery
import com.github.mukiva.openweather.core.data.response.GetForecastResponse

interface IForecastSource {

    /*
        Method for get current weather

        param q can be:
            - Latitude and Longitude
            - city name
            - US zip
            - UK postcode
            - Canada postal code|metar|iata|auto:ip IP lookup
            - IP address (IPv4 and IPv6 supported)
            - By ID returned from Search API
            - bulk
        param days is a number in 1..14 range
        param aqi can be:
            - yes
            - no
        param alerts can be:
            - yse
            - no
        param lang is a lang code e.g. ru or en
     */
    suspend fun getForecast(
        q: ParamQuery,
        days: ParamDays,
        aqi: ParamAqi,
        alerts: ParamAlerts,
        lang: ParamLang
    ): Result<GetForecastResponse>

}