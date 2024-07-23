package com.github.mukiva.weatherdata.editors

import com.github.mukiva.openweather.core.domain.settings.Group
import kotlinx.coroutines.flow.Flow

public interface INotificationGroupEditor {
    public suspend fun setCurrentWeatherNotificationEnabled(isEnabled: Boolean)
    public fun getCurrentWeatherNotificationEnabled(): Flow<Boolean>
    public fun getNotificationGroup(): Flow<Group.Notification>
}