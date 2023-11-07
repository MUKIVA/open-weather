package com.mukiva.openweather.data

abstract class Gateway(
    override val baseUrl: String
) : IGateway