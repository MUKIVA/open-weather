package com.github.mukiva.feature.dashboard.ui.dashboard_template_fragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.component.Component
import com.github.mukiva.feature.dashboard.R
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

internal class AstroComponent(
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
                .filterIsInstance(ICurrentState.Content::class)
                .map { state -> state.astro }
                .onEach(::onUpdateState)
                .launchIn(lifecycleOwner.lifecycleScope)
        }
    }

    private fun onUpdateState(state: Astro) = with(binding) {
        updateField(state.sunrise, fieldSunrice::text::set)
        updateField(state.sunset, fieldSunset::text::set)
        updateField(state.moonrise, fieldMoonrice::text::set)
        updateField(state.moonset, fieldMoonset::text::set)
    }

    private fun updateField(value: LocalTime?, fieldSetter: (String) -> Unit) {
        fieldSetter(value?.let { mTimeFormatter.format(it) }
            ?: binding.root.context.getString(R.string.no_data))
    }
}