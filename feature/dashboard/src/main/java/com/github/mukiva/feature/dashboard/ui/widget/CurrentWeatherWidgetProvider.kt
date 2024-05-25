package com.github.mukiva.feature.dashboard.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.os.bundleOf
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.core.ui.getWeatherDescription
import com.github.mukiva.core.ui.getWeatherRes
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.domain.model.ForecastDataWrapper
import com.github.mukiva.feature.dashboard.domain.usecase.GetCurrentWeatherUseCase
import com.github.mukiva.openweather.core.domain.weather.Temp
import com.github.mukiva.weatherdata.utils.RequestResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CurrentWeatherWidgetProvider : AppWidgetProvider() {
    sealed class State {
        data object Loading : State()

        data object Error : State()
        data object Empty : State()
        data class Content(
            val currentTemp: Temp,
            val locationName: String,
            val conditionCode: Int,
            val isDay: Boolean
        ) : State()
    }

    class Updater(
        private val context: Context?
    ) {
        fun update() {
            val applicationContext = context?.applicationContext ?: return
            val componentName = ComponentName(applicationContext, CurrentWeatherWidgetProvider::class.java)
            val widgetIds = AppWidgetManager.getInstance(applicationContext).getAppWidgetIds(componentName)
            val updateIntent = Intent(applicationContext, CurrentWeatherWidgetProvider::class.java)
                .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                .putExtras(bundleOf(AppWidgetManager.EXTRA_APPWIDGET_IDS to widgetIds))
            applicationContext.sendBroadcast(updateIntent)
        }
    }

    @Inject
    lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase

    private val mJob = SupervisorJob()
    private val mScope = CoroutineScope(Dispatchers.Main + mJob)

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.i("CurrentWeatherWidgetProvider", "onEnabled")
    }
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.i("CurrentWeatherWidgetProvider", "onUpdate")
        mScope.launch {
            getCurrentWeatherUseCase()
                .map(::asState)
                .onEach { state -> updateState(context, appWidgetManager, appWidgetIds, state) }
                .filter { state -> state !is State.Loading }
                .first()
        }
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        Log.i("CurrentWeatherWidgetProvider", "onRestored")
    }
    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.i("CurrentWeatherWidgetProvider", "onDeleted")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.i("CurrentWeatherWidgetProvider", "onDisabled")
    }

    private fun updateState(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?,
        state: State
    ) {
        if (context == null) return
        val remoteViews = when (state) {
            is State.Content -> asContent(context, state)
            State.Empty -> asEmpty(context)
            State.Error -> asError(context)
            State.Loading -> asLoading(context)
        }

        appWidgetIds?.forEach { widgetId ->
            appWidgetManager?.updateAppWidget(widgetId, remoteViews)
        }
    }
}

internal fun RemoteViews.updateStateVisibility(state: CurrentWeatherWidgetProvider.State) {
    setViewVisibility(R.id.errorState, boolAsViewVisibility(state is CurrentWeatherWidgetProvider.State.Error))
    setViewVisibility(R.id.emptyState, boolAsViewVisibility(state is CurrentWeatherWidgetProvider.State.Empty))
    setViewVisibility(R.id.loadingState, boolAsViewVisibility(state is CurrentWeatherWidgetProvider.State.Loading))
    setViewVisibility(R.id.contentState, boolAsViewVisibility(state is CurrentWeatherWidgetProvider.State.Content))
}

private fun asContent(context: Context, state: CurrentWeatherWidgetProvider.State.Content): RemoteViews {
    return RemoteViews(
        context.packageName,
        R.layout.lay_widget_current_weather
    ).apply {
        setTextViewText(R.id.locationName, state.locationName)
        setTextViewText(R.id.currentTemp, context.getTempString(state.currentTemp))
        setTextViewText(R.id.conditionText, context.getWeatherDescription(state.conditionCode))
        setImageViewResource(R.id.conditionImage, context.getWeatherRes(state.conditionCode, state.isDay))
        updateStateVisibility(state)
    }
}

internal fun asError(context: Context): RemoteViews {
    return RemoteViews(
        context.packageName,
        R.layout.lay_widget_current_weather
    ).apply { updateStateVisibility(CurrentWeatherWidgetProvider.State.Error) }
}

internal fun asEmpty(context: Context): RemoteViews {
    return RemoteViews(
        context.packageName,
        R.layout.lay_widget_current_weather
    ).apply { updateStateVisibility(CurrentWeatherWidgetProvider.State.Empty) }
}

internal fun asLoading(context: Context): RemoteViews {
    return RemoteViews(
        context.packageName,
        R.layout.lay_widget_current_weather
    ).apply { updateStateVisibility(CurrentWeatherWidgetProvider.State.Loading) }
}

internal fun asState(requestResult: RequestResult<ForecastDataWrapper>): CurrentWeatherWidgetProvider.State {
    return when (requestResult) {
        is RequestResult.Error -> {
            val data = checkNotNull(requestResult.data)
            when (data.errorType) {
                ForecastDataWrapper.ErrorType.NOTHING -> CurrentWeatherWidgetProvider.State.Error
                ForecastDataWrapper.ErrorType.GET_LOCATION_ERROR -> CurrentWeatherWidgetProvider.State.Error
                ForecastDataWrapper.ErrorType.LOCATION_NOT_FOUND -> CurrentWeatherWidgetProvider.State.Empty
            }
        }
        is RequestResult.InProgress -> CurrentWeatherWidgetProvider.State.Loading
        is RequestResult.Success -> {
            val data = checkNotNull(requestResult.data)
            val forecast = checkNotNull(data.data)
            val unitsType = checkNotNull(data.unitsType)
            CurrentWeatherWidgetProvider.State.Content(
                currentTemp = Temp(unitsType, forecast.current.tempC, forecast.current.tempF),
                locationName = forecast.location.name,
                isDay = forecast.current.isDay == 1,
                conditionCode = forecast.current.condition.code
            )
        }
    }
}

internal fun boolAsViewVisibility(isVisible: Boolean): Int {
    return when (isVisible) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}
