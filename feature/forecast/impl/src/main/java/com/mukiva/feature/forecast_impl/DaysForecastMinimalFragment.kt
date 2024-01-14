package com.mukiva.feature.forecast_impl

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mukiva.feature.forecast_impl.databinding.FragmentDaysForecastMinimalBinding
import com.mukiva.feature.forecast_impl.domain.Forecast
import com.mukiva.feature.forecast_impl.presentation.ForecastMinimalViewModel
import com.mukiva.feature.forecast_impl.presentation.MinimalForecastState
import com.mukiva.openweather.ui.emptyView
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.uiLazy
import com.mukiva.openweather.ui.viewBindings
import com.mukiva.openweather.ui.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DaysForecastMinimalFragment : Fragment(R.layout.fragment_days_forecast_minimal) {


    private val mBindings by viewBindings(FragmentDaysForecastMinimalBinding::bind)
    private val mViewModel by viewModels<ForecastMinimalViewModel>()
    private val mMinimalAdapter by uiLazy {
        MinimalForecastAdapter(
            onItemClick = {mViewModel.onItemClick(0)}
        )
    }
    private val mLocation by uiLazy {
        requireArguments().getString(ARG_LOCATION_KEY) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()

        subscribeOnViewModel()
    }

    private fun initList() = with(mBindings) {
        list.adapter = mMinimalAdapter
    }

    private fun subscribeOnViewModel() {
        lifecycleScope.launch {
            mViewModel.state
                .collect(::updateState)
        }
    }

    private fun updateState(state: MinimalForecastState) {
        updateType(state.type)
        updateList(state.forecastItems)
    }

    private fun updateList(items: List<Forecast>) {
        Log.d("DaysForecastMinimalFragment", "${items.size}")
        mMinimalAdapter.submitList(items)
    }

    private fun updateType(type: MinimalForecastState.Type) = with(mBindings) {
        when(type) {
            MinimalForecastState.Type.INIT -> {
                list.gone()
                mViewModel.loadForecast(mLocation)
            }
            MinimalForecastState.Type.EMPTY -> {
                list.gone()
                emptyView.emptyView(getString(R.string.get_forecast_error))
            }
            MinimalForecastState.Type.LOADING -> {
                list.gone()
                emptyView.loading()
            }
            MinimalForecastState.Type.CONTENT -> {
                list.visible()
                emptyView.hide()
            }
            MinimalForecastState.Type.ERROR -> {
                list.gone()
                emptyView.error(
                    getString(R.string.get_forecast_error),
                    getString(R.string.refresh)
                ) { mViewModel.loadForecast(mLocation) }
            }
        }
    }

    companion object {

        private const val ARG_LOCATION_KEY = "ARG_LOCATION_KEY"

        fun newInstance(
            location: String
        ) = DaysForecastMinimalFragment().apply {
            arguments = bundleOf(
                ARG_LOCATION_KEY to location
            )
        }
    }

}