package com.github.mukiva.feature.dashboard.ui.dashboard_template_fragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.github.mukiva.core.ui.component.Component
import com.github.mukiva.feature.dashboard.databinding.LayDayForecastBinding
import com.github.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.github.mukiva.feature.dashboard.presentation.ForecastState
import com.github.mukiva.feature.dashboard.presentation.ICurrentState
import com.github.mukiva.feature.dashboard.ui.adapter.MinimalForecastAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DayForecastComponent(
    private val binding: LayDayForecastBinding,
    private val locationId: Long,
    private val onItemClick: (Int) -> Unit
) : Component(), Component.IStateObserver<DashboardViewModel> {

    private val mAdapter by lazy {
        MinimalForecastAdapter(
            onItemClick = onItemClick
        )
    }

    override fun init() = with(binding) {
        val itemDecoration = MaterialDividerItemDecoration(
            binding.root.context,
            MaterialDividerItemDecoration.VERTICAL
        )
        forecastList.adapter = mAdapter
        forecastList.addItemDecoration(itemDecoration)
    }

    override fun subscribeOnViewModel(
        viewModel: DashboardViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.provideForecastState(locationId)
                .flowWithLifecycle(lifecycleOwner.lifecycle)
                .filterIsInstance(ICurrentState.Content::class)
                .map { state -> state.forecastState }
                .onEach(::onUpdateState)
                .launchIn(lifecycleOwner.lifecycleScope)
        }
    }

    private fun onUpdateState(state: ForecastState) {
        mAdapter.submitList(state.days)
    }
}