package com.github.mukiva.feature.dashboard.ui.dashboard_template_fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.KEY_ARGS
import com.github.mukiva.core.ui.component.component
import com.github.mukiva.core.ui.getArgs
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.databinding.FragmentDashboardTemplateBinding
import com.github.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.github.mukiva.feature.dashboard.presentation.ICurrentState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class DashboardTemplateFragment : Fragment(R.layout.fragment_dashboard_template) {

    @Parcelize
    data class Args(
        val locationId: Long
    ) : Parcelable

    private val mBinding by viewBindings(FragmentDashboardTemplateBinding::bind)
    private val mViewModel by viewModels<DashboardViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    private val mMainComponent by component {
        MainComponent(
            binding = mBinding.layMainInfo,
            locationId = getArgs(Args::class.java).locationId
        )
    }

    private val mPrecipitationComponent by component {
        PrecipitationComponent(
            binding = mBinding.layPrecipitation,
            locationId = getArgs(Args::class.java).locationId
        )
    }

    private val mAstroComponent by component {
        AstroComponent(
            binding = mBinding.layAstro,
            locationId = getArgs(Args::class.java).locationId
        )
    }

    private val mDayForecastComponent by component {
        DayForecastComponent(
            binding = mBinding.layDayForecast,
            locationId = getArgs(Args::class.java).locationId,
            onItemClick = { index ->
                mViewModel.goFullForecast(
                    locationId = getArgs(Args::class.java).locationId,
                    dayPosition = index
                )
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMainComponent.subscribeOnViewModel(mViewModel, viewLifecycleOwner)
        mPrecipitationComponent.subscribeOnViewModel(mViewModel, viewLifecycleOwner)
        mAstroComponent.subscribeOnViewModel(mViewModel, viewLifecycleOwner)
        mDayForecastComponent.init()
        mDayForecastComponent.subscribeOnViewModel(mViewModel, viewLifecycleOwner)

        subscribeRoot()
    }

    private fun subscribeRoot() {
        val locationId = getArgs(Args::class.java).locationId
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.provideForecastState(locationId)
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .filterIsInstance(ICurrentState.Init::class)
                .onEach { mViewModel.loadForecast(locationId) }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    companion object {
        fun newInstance(args: Args): DashboardTemplateFragment {
            return DashboardTemplateFragment().apply {
                arguments = bundleOf(KEY_ARGS to args)
            }
        }
    }

}
