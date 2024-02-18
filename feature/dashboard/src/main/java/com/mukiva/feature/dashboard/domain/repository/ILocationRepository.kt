package com.mukiva.feature.dashboard.domain.repository

import com.mukiva.feature.dashboard.domain.model.ILocation

interface ILocationRepository {
    suspend fun getAllLocations(): List<ILocation>

}