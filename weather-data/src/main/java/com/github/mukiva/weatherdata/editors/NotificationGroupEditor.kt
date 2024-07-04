package com.github.mukiva.weatherdata.editors

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.github.mukiva.openweather.core.domain.settings.CurrentWeather
import com.github.mukiva.openweather.core.domain.settings.Group
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class NotificationGroupEditor(
    private val dataStore: DataStore<Preferences>
) : INotificationGroupEditor {

    override suspend fun setCurrentWeatherNotificationEnabled(isEnabled: Boolean) {
        dataStore.edit { settings ->
            settings[CURRENT_WEATHER_NOTIFICATION_KEY] = isEnabled
        }
    }

    override fun getCurrentWeatherNotificationEnabled(): Flow<Boolean> {
        return dataStore.data.map { settings ->
            settings[CURRENT_WEATHER_NOTIFICATION_KEY] ?: false
        }
    }

    override fun getNotificationGroup(): Flow<Group.Notification> {
        val currentWeatherNotification = getCurrentWeatherNotificationEnabled()
        return currentWeatherNotification.map { isEnabled ->
            Group.Notification(
                currentWeather = CurrentWeather(isEnabled)
            )
        }
    }

    companion object {
        private val CURRENT_WEATHER_NOTIFICATION_KEY =
            booleanPreferencesKey("CURRENT_WEATHER_NOTIFICATION_KEY")
    }
}