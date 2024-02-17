package com.mukiva.openweather.glue

import android.content.Context
import com.mukiva.core.data.repository.IForecastUpdater
import com.mukiva.core.data.repository.settings.ISettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeForecastUpdater @Inject constructor(
    @ApplicationContext context: Context,
    private val settingsRepository: ISettingsRepository
) : IForecastUpdater {
    override val isTimeForUpdate: Boolean
        get() = mIsTimeForUpdate

    private val mShouldBeUpdatedFlow = MutableSharedFlow<Unit>()
    private val mCalendarInstance = Calendar.getInstance()
    private val mDateFormatter = SimpleDateFormat(
        "yyyy/MM/dd HH:mm", context.resources.configuration.locales[0]
    )
    private var mIsTimeForUpdate = false
    private val mUpdaterScope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        mUpdaterScope.launch {
            while (true) {
                val now = mCalendarInstance.time
                val lastUpdate = settingsRepository.getLastForecastUpdateTime()
                if (lastUpdate == null || hoursBetweenDates(now, lastUpdate) >= 1)
                    markForUpdate()
                delay(INSPECTION_INTERVAL_TIME_MS)
            }
        }
    }

    override fun markForUpdate() {
        mUpdaterScope.launch {
            mShouldBeUpdatedFlow.emit(Unit)
            mIsTimeForUpdate = true

        }
    }

    override fun commitUpdate() {
        mUpdaterScope.launch {
            val now = mCalendarInstance.time
            val nowStr = mDateFormatter.format(now)
            settingsRepository.setLastForecastUpdateTime(nowStr)
            mIsTimeForUpdate = false
        }
    }

    override fun observeUpdate(block: () -> Unit) {
        mUpdaterScope.launch {
            mShouldBeUpdatedFlow
                .collect { block() }
        }
    }

    private fun  hoursBetweenDates(now: Date, lastUpdate: String?): Long {
        if (lastUpdate == null) return 0
        val lastDate = mDateFormatter.parse(lastUpdate) ?: return 0
        val diffInMs = now.time - lastDate.time
        return diffInMs / (1000 * 60 * 60)
    }

    companion object {
        const val INSPECTION_INTERVAL_TIME_MS = 5 * 60 * 1000L
    }

}