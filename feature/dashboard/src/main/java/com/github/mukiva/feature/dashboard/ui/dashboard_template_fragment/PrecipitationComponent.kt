package com.github.mukiva.feature.dashboard.ui.dashboard_template_fragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.component.Component
import com.github.mukiva.core.ui.getPrecipitationString
import com.github.mukiva.feature.dashboard.databinding.LayPrecipitationBinding
import com.github.mukiva.feature.dashboard.domain.model.Precipitation
import com.github.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.github.mukiva.feature.dashboard.presentation.ICurrentState
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.github.mukiva.core.ui.R as CoreUiRes

class PrecipitationComponent(
    private val binding: LayPrecipitationBinding,
    private val locationId: Long
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
                .map { state -> state.precipitation }
                .onEach(::onUpdateState)
                .launchIn(lifecycleOwner.lifecycleScope)
        }
    }

    private fun onUpdateState(state: Precipitation) = with(binding) {
        val ctx = root.context
        fieldPrecipitation.text = root.getPrecipitationString(state.precipitationAmount)
        fieldWillItRain.text = root.formatBooleanValue(state.willItRain)
        fieldChanceOfRain.text = ctx.getString(CoreUiRes.string.template_percent, state.chanceOfRain)
        fieldWillItSnow.text = root.formatBooleanValue(state.willItSnow)
        fieldChanceOfSnow.text = ctx.getString(CoreUiRes.string.template_percent, state.chanceOfSnow)
    }

}