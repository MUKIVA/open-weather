package com.github.mukiva.weatherdata

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.mukiva.openweather.core.domain.settings.Config
import com.github.mukiva.openweather.core.domain.settings.Group
import com.github.mukiva.weatherdata.editors.GeneralGroupEditor
import com.github.mukiva.weatherdata.editors.IGeneralGroupEditor
import com.github.mukiva.weatherdata.editors.ILaunchGroupEditor
import com.github.mukiva.weatherdata.editors.INotificationGroupEditor
import com.github.mukiva.weatherdata.editors.LaunchGroupEditor
import com.github.mukiva.weatherdata.editors.NotificationGroupEditor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

public interface ISettingsRepository :
    IGeneralGroupEditor,
    ILaunchGroupEditor,
    INotificationGroupEditor {
    public fun getConfiguration(): Flow<Config>
}

internal class SettingsRepository(
    private val launchGroupEditor: ILaunchGroupEditor,
    private val notificationGroupEditor: INotificationGroupEditor,
    private val generalGroupEditor: IGeneralGroupEditor
) : ISettingsRepository,
    ILaunchGroupEditor by launchGroupEditor,
    INotificationGroupEditor by notificationGroupEditor,
    IGeneralGroupEditor by generalGroupEditor {

    override fun getConfiguration(): Flow<Config> {
        val general: Flow<Group> = getGeneralGroup()
        val currentWeather: Flow<Group> = getNotificationGroup()
        return general.combine(currentWeather) { generalGroup, currentGroup ->
            Config(listOf(generalGroup, currentGroup))
        }
    }
}

internal const val DATA_STORE_NAME = "SETTINGS"
internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)

public fun createSettingsRepository(
    context: Context
): ISettingsRepository {
    val applicationContext = context.applicationContext
        ?: error("Is not the applicationContext")

    val dataStore = applicationContext.dataStore
    val launchGroupEditor = LaunchGroupEditor(dataStore)
    val notificationGroupEditor = NotificationGroupEditor(dataStore)
    val generalGroupEditor = GeneralGroupEditor(dataStore)
    return SettingsRepository(
        launchGroupEditor = launchGroupEditor,
        notificationGroupEditor = notificationGroupEditor,
        generalGroupEditor = generalGroupEditor
    )
}
