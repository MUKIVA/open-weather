package com.github.mukiva.openweather.core.data.source

import com.github.mukiva.openweather.core.data.domain.params.ParamAqi
import com.github.mukiva.openweather.core.data.domain.params.ParamLang
import com.github.mukiva.openweather.core.data.domain.params.ParamQuery
import com.github.mukiva.openweather.core.data.response.GetCurrentResponse

/*
  Base source interface for get current weather
*/
interface IRealtimeSource {

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
        param aqi can be:
            - yes
            - no
        param lang is a lang code e.g. ru or en
     */
    suspend fun getCurrent(
        q: ParamQuery,
        aqi: ParamAqi,
        lang: ParamLang
    ): Result<GetCurrentResponse>
}