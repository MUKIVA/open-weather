package com.github.mukiva.openweather.core.data.source

import com.github.mukiva.openweather.core.data.domain.params.ParamLang
import com.github.mukiva.openweather.core.data.domain.params.ParamQuery
import com.github.mukiva.openweather.core.data.response.SearchResponse

interface ISearchSource {

    /*
        Method for get search result

        param q can be:
            - Latitude and Longitude
            - city name
            - US zip
            - UK postcode
            - Canada postal code|metar|iata|auto:ip IP lookup
            - IP address (IPv4 and IPv6 supported)
            - By ID returned from Search API
            - bulk
        param lang is a lang code e.g. ru or en
     */
    suspend fun getLocations(
        q: ParamQuery,
        lang: ParamLang
    ): Result<SearchResponse>

}