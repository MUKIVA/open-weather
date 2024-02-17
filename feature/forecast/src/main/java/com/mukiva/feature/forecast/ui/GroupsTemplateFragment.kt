package com.mukiva.feature.forecast.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.mukiva.core.ui.KEY_ARGS
import com.mukiva.core.ui.getArgs
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.feature.forecast.R
import com.mukiva.feature.forecast.databinding.FragmentGroupsTemplateBinding
import com.mukiva.feature.forecast.domain.IForecastGroup
import com.mukiva.feature.forecast.domain.IForecastItem
import com.mukiva.feature.forecast.ui.adapter.forecast.GroupsForecastAdapter
import java.io.Serializable

class GroupsTemplateFragment : Fragment(R.layout.fragment_groups_template) {

    data class Args(
        val groups: Collection<IForecastGroup<IForecastItem>>
    ): Serializable

    private val mBinding by viewBindings(FragmentGroupsTemplateBinding::bind)
    private val mGroupsAdapter by uiLazy { GroupsForecastAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList(getArgs(Args::class.java).groups)
    }

    private fun initList(groups: Collection<IForecastGroup<IForecastItem>>) = with(mBinding) {
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