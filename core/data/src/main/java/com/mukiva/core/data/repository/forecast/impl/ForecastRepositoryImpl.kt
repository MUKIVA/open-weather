package com.mukiva.core.data.repository.forecast.impl

import com.mukiva.core.data.repository.forecast.IForecastRepository
import com.mukiva.core.data.repository.forecast.entity.ForecastWithCurrentAndLocationRemote
import com.mukiva.core.data.repository.forecast.entity.ICurrentWeatherRemote
import com.mukiva.core.data.repository.forecast.gateway.IForecastGateway
import com.mukiva.core.network.IApiKeyProvider
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val gateway: IForecastGateway,
    private val apiKeyProvider: IApiKeyProvider
) : IForecastRepository {
    override suspend fun getCurrent(locationName: String): ICurrentWeatherRemote {
        val fullData = gateway.getCurrentWeather(
            key = apiKeyProvider.apiKey,
            q = locationName
        )
        return object : ICurrentWeatherRemote by fullData {}
    }

    override suspend fun getForecast(
        locationName: String,
        days: Int,
        aqi: Boolean,
        alerts: Boolean
    ): ForecastWithCurrentAndLocationRemote {
        return gateway.getCurrentWeather(
            key = apiKeyProvider.apiKey,
            q = locationName,
            days = days,
            aqi = if (aqi) "yes" else "no",
            alerts = if (alerts) "yes" else "no"
        )
    }
}