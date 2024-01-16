package com.mukiva.feature.forecast.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.mukiva.feature.forecast.R
import com.mukiva.feature.forecast.databinding.FragmentForecastBinding
import com.mukiva.feature.forecast.presentation.ForecastState
import com.mukiva.feature.forecast.presentation.ForecastViewModel
import com.mukiva.feature.forecast.ui.adapter.forecast.ForecastDayAdapter
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.openweather.ui.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    private val mBinding by viewBindings(FragmentForecastBinding::bind)
    private val mViewModel by viewModels<ForecastViewModel>()
    private val mForecastDayAdapter by uiLazy { ForecastDayAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        initSwipeRefreshLayout()

        subscribeOnViewModel()

    }

    private fun initSwipeRefreshLayout() = with(mBinding) {
        swipeRefreshLayout.setOnRefreshListener {
            mViewModel.loadForecast()
        }
    }

    private fun initViewPager() = with(mBinding) {
        viewPager.adapter = mForecastDayAdapter

        TabLayoutMediator(tabLayout, viewPager, true, true) { tab, index ->
            tab.text = mForecastDayAdapter[index].id.toString()
        }.attach()
    }

    private fun subscribeOnViewModel() {
        lifecycleScope.launch {
            mViewModel.state
                .flowWithLifecycle(lifecycle)
                .collect(::updateState)
        }
    }

    private fun updateState(state: ForecastState) {
        updateType(state.type)
    }

    private fun updateType(type: ForecastState.Type) = with(mBinding) {
        when(type) {
            ForecastState.Type.INIT -> {
                content.gone()
                swipeRefreshLayout.isEnabled = false
                emptyView.loading()
                mViewModel.loadForecast()
            }
            ForecastState.Type.ERROR -> {
                content.gone()
                swipeRefreshLayout.isEnabled = false
                emptyView.error(
                    getString(R.string.get_forecast_error),
                    getString(R.string.refresh)
                ) {
                    mViewModel.loadForecast()
                }
            }
            ForecastState.Type.CONTENT -> {
                emptyView.hide()
                swipeRefreshLayout.isEnabled = true
                content.visible()
            }
            ForecastState.Type.LOADING -> {
                emptyView.loading()
                swipeRefreshLayout.isEnabled = false
                content.gone()
            }
        }
    }
}