package com.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.di.DashboardComponent
import com.mukiva.feature.dashboard_impl.R
import com.mukiva.feature.dashboard_impl.databinding.FragmentCurrentWeatherBinding
import com.mukiva.openweather.ui.viewBindings
import com.presentation.DashboardViewModel
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_current_weather) {

    private val mViewModel: DashboardViewModel by viewModels {
        DashboardComponent.get().factory
    }

    private val mBinding by viewBindings(FragmentCurrentWeatherBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTitle()
        observeViewModel()
    }

    private fun initTitle() = with(mBinding) {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.select_locations -> {
                    mViewModel.onSelectLocations()
                    return@setOnMenuItemClickListener false
                }
                R.id.settings -> {
                    mViewModel.onSelectLocations()
                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }
        toolbar.title = "London, 18℃"
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mViewModel.state
                .flowWithLifecycle(lifecycle)
                .collect { updateFragmentState() }
        }
    }

    private fun updateFragmentState() {
    }

    companion object {
        fun newInstance() = DashboardFragment()
    }
}