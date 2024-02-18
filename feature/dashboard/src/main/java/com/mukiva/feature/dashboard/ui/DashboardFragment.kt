package com.mukiva.feature.dashboard.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.mukiva.feature.dashboard.R
import com.mukiva.feature.dashboard.databinding.FragmentDashboardBinding
import com.mukiva.feature.dashboard.domain.model.CurrentWithLocation
import com.mukiva.feature.dashboard.domain.model.UnitsType
import com.mukiva.feature.dashboard.presentation.DashboardState
import com.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.mukiva.feature.dashboard.ui.adapter.DashboardAdapter
import com.mukiva.openweather.ui.dp
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.notify
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.openweather.ui.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import com.mukiva.core.ui.R as CoreUiRes

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val mViewModel by viewModels<DashboardViewModel>()
    private val mBinding by viewBindings(FragmentDashboardBinding::bind)
    private val mDashboardAdapter by uiLazy { DashboardAdapter(childFragmentManager, lifecycle) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTitle()
        initAppbar()
        initDashboard()
        observeViewModel()
    }

    private fun initDashboard() = with(mBinding) {
        dashboard.adapter = mDashboardAdapter

        dashboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                ifResumed { mViewModel.onPageSelect(position) }
            }
        })

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                dashboard.setCurrentItem(mViewModel.position, false)
            }
        }

    }

    private fun initAppbar() = with(mBinding) {

        toolbarLayout.setExpanded(!mViewModel.toolbarIsCollapsed, false)

        toolbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val maxOffset = appBarLayout.totalScrollRange
            val offsetRatio = abs(verticalOffset.toFloat() / maxOffset)
            val defPadding = requireContext().resources
                .getDimensionPixelOffset(CoreUiRes.dimen.def_h_padding)

            val padding = (MAIN_CARD_FADE_RATIO - offsetRatio) * defPadding
            mainCard.root.cardElevation = padding
            dashboardContainer.radius = padding * 2f
            dashboard.updatePadding(top = padding.toInt() + dp(8))
            dragHandler.alpha = max(min(MAX_DRAG_HANDLER_ALPHA, MAX_DRAG_HANDLER_ALPHA - offsetRatio), MIN_DRAG_HANDLER_ALPHA)

            mViewModel.toolbarIsCollapsed = maxOffset == abs(verticalOffset)
        }
    }

    private fun initTitle() = with(mBinding) {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.select_locations -> {
                    mViewModel.onSelectLocations()
                    return@setOnMenuItemClickListener false
                }
                R.id.settings -> {
                    mViewModel.onSettings()
                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mViewModel.state
                .flowWithLifecycle(lifecycle)
                .collect { state ->
                    updateFragmentState(state)
                }
        }
    }

    private fun updateFragmentState(state: DashboardState) {
        updateType(state.type)
        if (state.locationCount == 0) return
        updateDashboard(state.locationCount)
        if (state.currentWeather == null) return
        updateTitle(state.currentWeather, state.unitsType)
    }

    private fun updateDashboard(count: Int) = with(mBinding) {
        mDashboardAdapter.submit(count)
        if (mBinding.dashboard.adapter == null)
            mBinding.dashboard.adapter = mDashboardAdapter
        dashboard.offscreenPageLimit = max(count - 1, 0)
    }

    private fun updateTitle(currentWithLocation: CurrentWithLocation, unitsType: UnitsType) = with(mBinding) {

        val mainCardTemp: (UnitsType) -> String = { unitsType ->
            when(unitsType) {
                UnitsType.METRIC -> getString(
                    R.string.template_celsius_main_card, currentWithLocation.currentWeather?.tempC?.toInt()
                )
                UnitsType.IMPERIAL -> getString(
                    R.string.template_fahrenheit_main_card, currentWithLocation.currentWeather?.tempF?.toInt()
                )
            }
        }

        val createTitle: (UnitsType) -> String = { unitsType ->
            when(unitsType) {
                UnitsType.METRIC -> getString(
                    R.string.template_celsius_main_title,
                    currentWithLocation.currentWeather?.tempC?.toInt(), currentWithLocation.location.name
                )
                UnitsType.IMPERIAL -> getString(
                    R.string.template_fahrenheit_main_title,
                    currentWithLocation.currentWeather?.tempF?.toInt(), currentWithLocation.location.name
                )
            }
        }

        val title = createTitle(unitsType)

        toolbar.title = title
        collapsingAppbarLayout.title = title
        mainCard.mainTemp.text = mainCardTemp(unitsType)
        mainCard.cityName.text = currentWithLocation.location.name
    }

    private fun updateType(type: DashboardState.Type) = with(mBinding) {

        val setContentVisible: (View.() -> Unit) -> Unit = {
            toolbarLayout.it()
            dashboardContainer.it()
        }

        when(type) {
            DashboardState.Type.INIT -> {
                emptyView.loading()
                setContentVisible(View::gone)
                mViewModel.load()
            }
            DashboardState.Type.LOADING -> {
                emptyView.loading()
                setContentVisible(View::gone)
            }
            DashboardState.Type.CONTENT -> {
                emptyView.hide()
                setContentVisible(View::visible)
            }
            DashboardState.Type.ERROR -> {
                emptyView.error("TODO: ADD ERROR TEXT", "TODO: REFRESH") {
                    mViewModel.load()
                }
                setContentVisible(View::gone)
            }

            DashboardState.Type.EMPTY -> {
                emptyView.notify("Please, add your city", "Search") { mViewModel.onSelectLocations() }
                setContentVisible(View::gone)
            }
        }
    }

    override fun onStop() = with(mBinding) {
        super.onStop()
        dashboard.adapter = null
    }

    private fun Fragment.ifResumed(block: () -> Unit) {
        if (lifecycle.currentState == Lifecycle.State.RESUMED) {
            block()
        }
    }

    companion object {

        private const val MAX_DRAG_HANDLER_ALPHA = 0.5f
        private const val MIN_DRAG_HANDLER_ALPHA = 0.0f

        private const val MAIN_CARD_FADE_RATIO = 0.35f

        fun newInstance() = DashboardFragment()
    }
}