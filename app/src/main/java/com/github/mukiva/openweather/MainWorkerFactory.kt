package com.github.mukiva.openweather

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.github.mukiva.feature.weathernotification.WeatherNotificationWorker
import javax.inject.Inject

internal class MainWorkerFactory @Inject constructor(
    private val weatherNotificationWorkerFactory: WeatherNotificationWorker.Factory
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return when(workerClassName) {
            WeatherNotificationWorker::class.java.name -> weatherNotificationWorkerFactory
                .create(appContext, workerParameters)
            else -> error("Could not create worker with $workerClassName class name")
        }
    }
}