package com.github.mukiva.feature.dashboard.ui.dashboard_template_fragment

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.component.Component
import com.github.mukiva.feature.dashboard.databinding.LayAstroBinding
import com.github.mukiva.feature.dashboard.domain.model.Astro
import com.github.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.github.mukiva.feature.dashboard.presentation.ICurrentState
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

class AstroComponent(
    private val binding: LayAstroBinding,
    private val locationId: Long
) : Component(), Component.IStateObserver<DashboardViewModel> {

    private val mTimeFormatter = LocalTime.Format {
        hour(Padding.ZERO);char(':');minute(Padding.ZERO)
    }

    override fun init() {}

    override fun subscribeOnViewModel(
        viewModel: DashboardViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.provideForecastState(locationId)
                .flowWithLifecycle(lifecycleOwner.lifecycle)
                .onEach {
                    Log.d("STATE", "$it")
                }
                .filterIsInstance(ICurrentState.Content::class)
                .map { state -> state.astro }
                .onEach(::onUpdateState)
                .launchIn(lifecycleOwner.lifecycleScope)
        }
    }

    private fun onUpdateState(state: Astro) = with(binding) {
        fieldSunrice.text = mTimeFormatter.format(state.sunrise)
        fieldSunset.text = mTimeFormatter.format(state.sunset)
        fieldMoonrice.text = mTimeFormatter.format(state.moonrise)
        fieldMoonset.text = mTimeFormatter.format(state.moonset)
    }
}