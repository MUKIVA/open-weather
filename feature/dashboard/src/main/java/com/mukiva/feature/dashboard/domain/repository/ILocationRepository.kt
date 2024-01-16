package com.mukiva.feature.dashboard.domain.repository

import com.mukiva.feature.dashboard.domain.model.Location

interface ILocationRepository {
    suspend fun getAllLocations(): List<Location>

}