package com.mukiva.feature.forecast.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mukiva.core.ui.KEY_ARGS
import com.mukiva.core.ui.getArgs
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.feature.forecast.R
import com.mukiva.feature.forecast.databinding.FragmentGroupsTemplateBinding
import com.mukiva.feature.forecast.domain.UnitsType
import com.mukiva.feature.forecast.presentation.ForecastGroup
import com.mukiva.feature.forecast.presentation.ForecastViewModel
import com.mukiva.feature.forecast.ui.adapter.forecast.GroupsForecastAdapter
import java.io.Serializable

class GroupsTemplateFragment : Fragment(R.layout.fragment_groups_template) {

    data class Args(
        val locationName: String,
        val groups: Collection<ForecastGroup>,
        val unitsType: UnitsType
    ): Serializable

    private val mViewModel by viewModels<ForecastViewModel>(ownerProducer = {
        requireParentFragment()
    })
    private val mBinding by viewBindings(FragmentGroupsTemplateBinding::bind)
    private val mGroupsAdapter by uiLazy {
        GroupsForecastAdapter(
            unitsType = getArgs(Args::class.java).unitsType
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList(getArgs(Args::class.java).groups)
        initSwipeRefreshLayout()
    }

    private fun initSwipeRefreshLayout() = with(mBinding) {
        swipeRefreshLayout.setOnRefreshListener {
            mViewModel.loadForecast(getArgs(Args::class.java).locationName)
        }
    }

    private fun initList(groups: Collection<ForecastGroup>) = with(mBinding) {
        content.adapter = mGroupsAdapter
        mGroupsAdapter.submitList(groups.toList())
    }

    companion object {
        fun newInstance(args: Args) = GroupsTemplateFragment().apply {
            arguments = bundleOf(
                KEY_ARGS to args
            )
        }
    }

}