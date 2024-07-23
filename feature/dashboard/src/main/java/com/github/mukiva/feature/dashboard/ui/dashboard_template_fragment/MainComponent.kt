package com.github.mukiva.feature.dashboard.ui.dashboard_template_fragment

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.component.Component
import com.github.mukiva.core.ui.getPressureString
import com.github.mukiva.core.ui.getSpeedString
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.databinding.LayMainInfoBinding
import com.github.mukiva.feature.dashboard.domain.model.Current
import com.github.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.github.mukiva.feature.dashboard.presentation.ICurrentState
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.github.mukiva.core.ui.R as CoreUiRes

class MainComponent(
    private val binding: LayMainInfoBinding,
    private val locationId: Long,
) : Component(), Component.IStateObserver<DashboardViewModel> {

    override fun init() {}

    override fun subscribeOnViewModel(
        viewModel: DashboardViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.provideForecastState(locationId)
                .flowWithLifecycle(lifecycleOwner.lifecycle)
                .filterIsInstance(ICurrentState.Content::class)
                .map { state -> state.currentState }
                .onEach(::onUpdateState)
                .launchIn(lifecycleOwner.lifecycleScope)
        }
    }

    private fun onUpdateState(state: Current) = with(binding) {
        val ctx = root.context
        fieldIsDay.text = root.formatIsDayValue(state.isDay)
        fieldCloud.text = ctx.getString(CoreUiRes.string.template_percent, state.cloudPercent)
        fieldHumidity.text = ctx.getString(CoreUiRes.string.template_percent, state.humidityPercent)
        fieldPressure.text = root.getPressureString(state.pressure)
        fieldFeelsLike.text = root.getTempString(state.feelsLike)
        fieldWindSpeed.text = root.getSpeedString(state.windSpeed)
        fieldWindDirection.text = state.windDirection.toString()
    }
}

internal fun View.formatBooleanValue(value: Boolean): String = with(context) {
    return when (value) {
        true -> getString(R.string.yes)
        false -> getString(R.string.no)
    }
}

internal fun View.formatIsDayValue(isDay: Boolean): String = with(context) {
    return when (isDay) {
        true -> getString(R.string.is_day_true)
        false -> getString(R.string.is_day_false)
    }
}