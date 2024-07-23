package com.github.mukiva.feature.dashboard.ui.dashboard_fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.component.Component
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.core.ui.getWeatherRes
import com.github.mukiva.core.ui.hide
import com.github.mukiva.core.ui.loading
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.databinding.LayDashboardActionBarBinding
import com.github.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.github.mukiva.feature.dashboard.presentation.IActionBarState
import com.github.mukiva.openweather.core.domain.settings.UnitsType
import com.github.mukiva.openweather.core.domain.weather.Temp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.math.roundToInt

class ActionBarComponent(
    private val binding: LayDashboardActionBarBinding,
    private val onSettingsClick: () -> Unit,
    private val onManageLocationsClick: () -> Unit,
    private val onReloadClick: () -> Unit,
) : Component(), Component.IStateObserver<DashboardViewModel> {

    override fun init() {
        initActionMenu()
    }

    private fun initActionMenu() = with(binding) {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.select_locations -> {
                    onManageLocationsClick()
                    return@setOnMenuItemClickListener true
                }
                R.id.settings -> {
                    onSettingsClick()
                    return@setOnMenuItemClickListener true
                }
                R.id.refresh -> {
                    onReloadClick()
                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }
    }

    override fun subscribeOnViewModel(
        viewModel: DashboardViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModel.state
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .map { state -> state.actionBarState }
            .onEach(::onUpdateState)
            .launchIn(lifecycleOwner.lifecycleScope)
    }

    private fun onUpdateState(state: IActionBarState) {
        when  (state) {
            is IActionBarState.Content -> setContentState(state)
            IActionBarState.Loading -> setLoadingState()
        }
    }

    private fun setLoadingState() = with(binding) {
        layMainActionInfo.emptyView.loading()
        layMainActionInfo.temperature.visibility = View.INVISIBLE
        layMainActionInfo.location.visibility = View.INVISIBLE
        layMainActionInfo.conditionImage.visibility = View.INVISIBLE
    }

    private fun setContentState(data: IActionBarState.Content) = with(binding) {
        layMainActionInfo.emptyView.hide()

        layMainActionInfo.temperature.visible()
        layMainActionInfo.location.visible()
        layMainActionInfo.conditionImage.visible()

        layMainActionInfo.temperature.text = root.context.getTempString(data.temp)
        layMainActionInfo.location.text = data.location
        collapsingAppbarLayout.title = root.getMainTitle(data.location, data.temp)
        layMainActionInfo.conditionImage.setImageResource(
            root.context.getWeatherRes(data.imageCode, data.isDay)
        )
    }
}

internal fun View.getMainTitle(
    locationName: String,
    temp: Temp,
): String {
    return when (temp.unitsType) {
        UnitsType.METRIC ->
            context.getString(R.string.template_celsius_main_title, temp.value.roundToInt(), locationName)
        UnitsType.IMPERIAL ->
            context.getString(R.string.template_fahrenheit_main_title, temp.value.roundToInt(), locationName)
    }
}