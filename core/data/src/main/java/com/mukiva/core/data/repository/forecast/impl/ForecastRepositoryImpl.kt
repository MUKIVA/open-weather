package com.mukiva.core.data.repository.forecast.impl

import com.mukiva.core.data.repository.IForecastUpdater
import com.mukiva.core.data.repository.forecast.IForecastRepository
import com.mukiva.core.data.repository.forecast.entity.ForecastWithCurrentAndLocationRemote
import com.mukiva.core.data.repository.forecast.gateway.IForecastGateway
import com.mukiva.core.network.IApiKeyProvider
import javax.inject.Inject
import javax.inject.Singleton

typealias LocationName = String

@Singleton
class ForecastRepositoryImpl @Inject constructor(
    private val gateway: IForecastGateway,
    private val updater: IForecastUpdater
) : IForecastRepository {

    private val mLocationCache = HashMap<LocationName, ForecastWithCurrentAndLocationRemote>()

    override suspend fun getCurrent(locationName: String): ForecastWithCurrentAndLocationRemote {

        if (updater.isTimeForUpdate || mLocationCache[locationName] == null) {
            mLocationCache[locationName] = gateway.getCurrentWeather(
                q = locationName
            )
            updater.commitUpdate()
        }

        if (mLocationCache[locationName] == null)
            error("Failed to get forecast")

        return mLocationCache[locationName]!!
    }

    override suspend fun getForecast(
        locationName: String,
        days: Int,
        aqi: Boolean,
        alerts: Boolean
    ): ForecastWithCurrentAndLocationRemote {

        if (updater.isTimeForUpdate || mLocationCache[locationName] == null) {
            mLocationCache[locationName] = gateway.getCurrentWeather(
                q = locationName,
                days = days,
                aqi = if (aqi) "yes" else "no",
                alerts = if (alerts) "yes" else "no"
            )
            updater.commitUpdate()
        }

        return mLocationCache[locationName]!!
    }

}