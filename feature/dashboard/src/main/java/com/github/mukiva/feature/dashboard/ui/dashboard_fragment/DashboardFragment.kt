package com.github.mukiva.feature.dashboard.ui.dashboard_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.component.component
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.databinding.FragmentDashboardBinding
import com.github.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val mViewModel by viewModels<DashboardViewModel>()
    private val mBinding by viewBindings(FragmentDashboardBinding::bind)

    private val mActionBarComponent by component {
        ActionBarComponent(
            binding = mBinding.layActionBar,
            onSettingsClick = mViewModel::goSettings,
            onManageLocationsClick = mViewModel::goLocationManager,
            onReloadClick = {
                lifecycleScope.launch {
                    mViewModel.requestLoad()
                    mBinding.viewPager.currentItem = 0
                    mViewModel.selectPage(0)
                }
            }
        )
    }

    private val mPagerComponent by component {
        PagerComponent(
            binding = mBinding,
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
            onPageChanged = { index ->
                lifecycleScope.launch {
                    mViewModel.selectPage(index)
                }
                            },
            onEmptyAction = mViewModel::goLocationManager,
            onErrorAction = mViewModel::requestLoad,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActionBarComponent.init()
        mActionBarComponent.subscribeOnViewModel(mViewModel, viewLifecycleOwner)

        mPagerComponent.init()
        mPagerComponent.subscribeOnViewModel(mViewModel, viewLifecycleOwner)

        initTabLayout()
    }

    private fun initTabLayout() = with(mBinding) {
        TabLayoutMediator(layActionBar.tabs, viewPager, true, true) { _, _ -> }
            .attach()
    }
}
