package com.mukiva.feature.forecast.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mukiva.core.ui.KEY_ARGS
import com.mukiva.core.ui.getArgs
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.feature.forecast.R
import com.mukiva.feature.forecast.databinding.FragmentGroupsTemplateBinding
import com.mukiva.feature.forecast.presentation.ForecastViewModel
import com.mukiva.feature.forecast.ui.adapter.forecast.GroupsForecastAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.Serializable

@AndroidEntryPoint
class GroupsTemplateFragment : Fragment(R.layout.fragment_groups_template) {

    data class Args(
        val position: Int,
    ): Serializable

    private val mViewModel by viewModels<ForecastViewModel>(ownerProducer = {
        requireParentFragment()
    })
    private val mBinding by viewBindings(FragmentGroupsTemplateBinding::bind)
    private val mGroupsAdapter by uiLazy {
        GroupsForecastAdapter()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        mViewModel.dayState(getArgs(Args::class.java).position)
            .flowWithLifecycle(lifecycle)
            .onEach { (unitsType, hourlyForecast) ->
                mGroupsAdapter.updateUnitsType(unitsType)
                mGroupsAdapter.submitList(hourlyForecast.groups)
            }
            .launchIn(lifecycleScope)
    }

    private fun initList() = with(mBinding) {
        content.adapter = mGroupsAdapter
    }

    companion object {
        fun newInstance(
            args: Args
        ) = GroupsTemplateFragment().apply {
            arguments = bundleOf(
                KEY_ARGS to args
            )
        }
    }

}