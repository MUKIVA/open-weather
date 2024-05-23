package com.github.mukiva.feature.weathernotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.github.mukiva.weatherdata.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WeatherNotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var serviceLauncher: IWeatherNotificationServiceLauncher

    private val mBroadcastReceiverScope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent == null) return
        when(intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                mBroadcastReceiverScope.launch {
                    val isEnabled = settingsRepository.getCurrentWeatherNotificationEnabled()
                        .first()
                    Log.i("WeatherNotificationBroadcastReceiver", "IS ENABLED: $isEnabled")
                    if (isEnabled) {
                        serviceLauncher.startService()
                    }
                }
            }
        }
    }
}
