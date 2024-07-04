package com.github.mukiva.weatherdata.editors

import com.github.mukiva.openweather.core.domain.settings.Group
import kotlinx.coroutines.flow.Flow

interface INotificationGroupEditor {
    suspend fun setCurrentWeatherNotificationEnabled(isEnabled: Boolean)

    fun getCurrentWeatherNotificationEnabled(): Flow<Boolean>

    fun getNotificationGroup(): Flow<Group.Notification>
}