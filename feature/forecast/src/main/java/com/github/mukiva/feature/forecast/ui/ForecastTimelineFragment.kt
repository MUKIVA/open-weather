package com.github.mukiva.feature.forecast.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.github.mukiva.core.ui.KEY_ARGS
import com.github.mukiva.core.ui.getArgs
import com.github.mukiva.core.ui.getDimen
import com.github.mukiva.core.ui.gone
import com.github.mukiva.core.ui.hide
import com.github.mukiva.core.ui.loading
import com.github.mukiva.core.ui.uiLazy
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.forecast.R
import com.github.mukiva.feature.forecast.databinding.FragmentForecastTimelineBinding
import com.github.mukiva.feature.forecast.presentation.ForecastViewModel
import com.github.mukiva.feature.forecast.presentation.HourlyForecast
import com.github.mukiva.feature.forecast.ui.adapter.ForecastItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.Serializable
import com.github.mukiva.core.ui.R as CoreUiRes

@AndroidEntryPoint
class ForecastTimelineFragment : Fragment(R.layout.fragment_forecast_timeline) {

    data class Args(
        val position: Int,
    ) : Serializable

    private val mViewModel by uiLazy {
        viewModels<ForecastViewModel>(ownerProducer = { requireParentFragment() })
            .value.getStateHolder(getArgs(Args::class.java).position)
    }
    private val mBinding by viewBindings(FragmentForecastTimelineBinding::bind)
    private val mHoursAdapter by uiLazy { ForecastItemAdapter() }
    private val mItemDecoration = object : RecyclerView.ItemDecoration() {

        private val mTopPadding by lazy { getDimen(CoreUiRes.dimen.def_v_padding) }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            with(outRect) {
                top = mTopPadding
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        mViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::updateState)
            .launchIn(lifecycleScope)
    }

    private fun initList() = with(mBinding) {
        list.setRecycledViewPool(mSharedViewPool)
        list.addItemDecoration(mItemDecoration)
        list.adapter = mHoursAdapter

        ViewCompat.setOnApplyWindowInsetsListener(list) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = systemBarsInsets.bottom)
            insets
        }
    }

    private fun updateState(state: HourlyForecast) {
        when (state) {
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
