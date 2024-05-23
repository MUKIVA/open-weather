package com.github.mukiva.navigation.ui

import android.util.Log
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.openweather.core.domain.settings.Lang
import com.github.mukiva.openweather.core.domain.settings.Theme
import com.github.mukiva.weatherdata.SettingsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsHandler @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val weatherNotificationServiceLauncher: IWeatherNotificationServiceLauncher
) : IOnCreateHandler, IOnDestroyHandler {

    private var mActivity: FragmentActivity? = null

    @MainThread
    private fun updateAppTheme(theme: Theme) {
        if (mActivity == null) return
        when (theme) {
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

    @MainThread
    private fun updateLocalization(lang: Lang) {
        if (mActivity == null) return
        when (lang) {
            Lang.SYSTEM -> {
                val localeList = LocaleListCompat.forLanguageTags("xx")
                AppCompatDelegate.setApplicationLocales(localeList)
            }
            Lang.EN -> AppCompatDelegate
                .setApplicationLocales(LocaleListCompat.create(Locale(lang.code)))
            Lang.RU -> AppCompatDelegate
                .setApplicationLocales(LocaleListCompat.create(Locale(lang.code)))
        }
    }

    private fun updateCurrentWeatherNotification(isEnabled: Boolean) {
        Log.i("SettingsHandler", "IS ENABLED: $isEnabled")
        when (isEnabled) {
            true -> weatherNotificationServiceLauncher.startService()
            false -> weatherNotificationServiceLauncher.stopService()
        }
    }

    override fun onCreated(activity: FragmentActivity) {
        mActivity = activity

        settingsRepository.getTheme()
            .flowWithLifecycle(activity.lifecycle)
            .onEach(::updateAppTheme)
            .launchIn(activity.lifecycleScope)

        settingsRepository.getLocalization()
            .flowWithLifecycle(activity.lifecycle)
            .onEach(::updateLocalization)
            .launchIn(activity.lifecycleScope)

        settingsRepository.getCurrentWeatherNotificationEnabled()
            .flowWithLifecycle(activity.lifecycle)
            .onEach(::updateCurrentWeatherNotification)
            .launchIn(activity.lifecycleScope)
    }

    override fun onDestroy() {
        mActivity = null
    }
}

