package com.github.mukiva.feature.forecast.ui

import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
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
import com.github.mukiva.core.ui.uiLazy
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.forecast.R
import com.github.mukiva.feature.forecast.databinding.FragmentForecastTimelineBinding
import com.github.mukiva.feature.forecast.presentation.ForecastState
import com.github.mukiva.feature.forecast.presentation.ForecastViewModel
import com.github.mukiva.feature.forecast.presentation.HourlyForecast
import com.github.mukiva.feature.forecast.ui.adapter.ForecastItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import com.github.mukiva.core.ui.R as CoreUiRes

@AndroidEntryPoint
class ForecastTimelineFragment : Fragment(R.layout.fragment_forecast_timeline) {

    @Parcelize
    data class Args(
        val id: Long,
        val position: Int
    ) : Parcelable

    private val mViewModel by viewModels<ForecastViewModel>(
        ownerProducer = { requireParentFragment() }
    )
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
            .filterIsInstance<ForecastState.Content>()
            .mapNotNull { forecast ->
                forecast.hourlyForecast.getOrNull(getArgs(Args::class.java).position)
            }
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

    private fun updateState(state: HourlyForecast) = with(mBinding) {
        list.visible()
        mHoursAdapter.submitList(state.hours)
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
