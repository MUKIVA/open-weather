package com.mukiva.feature.forecast.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.mukiva.core.ui.KEY_ARGS
import com.mukiva.core.ui.getArgs
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.feature.forecast.R
import com.mukiva.feature.forecast.databinding.FragmentForecastTimelineBinding
import com.mukiva.feature.forecast.presentation.ForecastViewModel
import com.mukiva.feature.forecast.presentation.HourlyForecast
import com.mukiva.feature.forecast.ui.adapter.forecast.ForecastItemAdapter
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.Serializable

@AndroidEntryPoint
class ForecastTimelineFragment : Fragment(R.layout.fragment_forecast_timeline) {

    data class Args(
        val position: Int,
    ): Serializable

    private val mViewModel by uiLazy {
        viewModels<ForecastViewModel>(ownerProducer = { requireParentFragment() })
            .value.getStateHolder(getArgs(Args::class.java).position)
    }
    private val mBinding by viewBindings(FragmentForecastTimelineBinding::bind)
    private val mHoursAdapter by uiLazy { ForecastItemAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        mViewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach(::updateState)
            .launchIn(lifecycleScope)
    }

    private fun initList() = with(mBinding) {
        list.setRecycledViewPool(mSharedViewPool)
        list.adapter = mHoursAdapter
    }

    private fun updateState(state: HourlyForecast) {
        when(state) {
            is HourlyForecast.Content -> with(mBinding) {
                emptyView.hide()
                list.visible()
                mHoursAdapter.submitList(state.hours)
            }
            HourlyForecast.Init -> with(mBinding) {
                emptyView.loading()
                list.gone()
                mViewModel.loadHours(getArgs(Args::class.java).position)
            }
        }

    }

    companion object {
        private val mSharedViewPool = RecycledViewPool()

        fun newInstance(
            args: Args
        ) = ForecastTimelineFragment().apply {
            arguments = bundleOf(
                KEY_ARGS to args
            )
        }
    }

}