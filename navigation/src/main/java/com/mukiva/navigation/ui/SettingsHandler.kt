package com.mukiva.navigation.ui

import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.weather_data.SettingsRepository
import com.github.mukiva.open_weather.core.domain.settings.Theme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsHandler @Inject constructor(
    private val settingsRepository: SettingsRepository
) : AbstractLifecycleHandler() {

    private var mActivity: FragmentActivity? = null

    @MainThread
    private fun updateAppTheme(theme: Theme) {
        if (mActivity == null) return
        when(theme) {
            Theme.SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            Theme.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            Theme.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreated(activity: FragmentActivity) {
        super.onCreated(activity)
        mActivity = activity

        settingsRepository.getTheme()
            .flowWithLifecycle(activity.lifecycle)
            .onEach(::updateAppTheme)
            .launchIn(activity.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity = null
    }

}