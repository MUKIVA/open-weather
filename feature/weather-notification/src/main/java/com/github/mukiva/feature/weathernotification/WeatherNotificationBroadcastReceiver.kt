package com.github.mukiva.feature.weathernotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.github.mukiva.weatherdata.ISettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class WeatherNotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsRepository: ISettingsRepository

    @Inject
    lateinit var serviceLauncher: IWeatherNotificationServiceLauncher

    private val mBroadcastReceiverScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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
