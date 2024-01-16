package com.mukiva.navigation.ui

import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import com.mukiva.navigation.domain.repository.ISettingsRepository
import com.mukiva.navigation.domain.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsHandler @Inject constructor(
    private val settingsRepository: ISettingsRepository
) : AbstractLifecycleHandler() {

    private var mActivity: FragmentActivity? = null
    private var mJob: Job? = null

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

        mJob = CoroutineScope(Job() + Dispatchers.Main).launch {
            settingsRepository.getThemeFlow()
                .collect(::updateAppTheme)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob?.cancel()
        mJob = null
        mActivity = null
    }

}