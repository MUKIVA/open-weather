package com.github.mukiva.feature.dashboard.ui

import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.mukiva.core.ui.error
import com.github.mukiva.core.ui.getDimen
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.core.ui.gone
import com.github.mukiva.core.ui.hide
import com.github.mukiva.core.ui.lazyAdapter
import com.github.mukiva.core.ui.loading
import com.github.mukiva.core.ui.notify
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.databinding.FragmentDashboardBinding
import com.github.mukiva.feature.dashboard.presentation.DashboardState
import com.github.mukiva.feature.dashboard.presentation.MainCardState
import com.github.mukiva.feature.dashboard.presentation.SharedDashboardViewModel
import com.github.mukiva.feature.dashboard.ui.adapter.DashboardAdapter
import com.github.mukiva.openweather.core.domain.settings.UnitsType
import com.github.mukiva.openweather.core.domain.weather.Temp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.abs
import kotlin.math.roundToInt
import com.github.mukiva.core.ui.R as CoreUiRes

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val mViewModel by viewModels<SharedDashboardViewModel>()
    private val mBinding by viewBindings(FragmentDashboardBinding::bind)
    private val mDashboardAdapter by lazyAdapter {
        DashboardAdapter(childFragmentManager, lifecycle)
    }
    private var mIsAppbarExpanded: Boolean = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTitle()
        initDashboard()
        initAppbar()
        observeViewModel()
    }

    private fun initDashboard() = with(mBinding) {
        dashboard.adapter = mDashboardAdapter
        dashboard.offscreenPageLimit = 2
        dashboard.children.find { child -> child is RecyclerView }?.apply {
            (this as RecyclerView).isNestedScrollingEnabled = false
        }
        toolbarLayout.setExpanded(mIsAppbarExpanded, false)
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.root) { _, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            dashboard.updatePadding(bottom = systemBarsInsets.bottom)
            insets
        }
    }

    private fun initAppbar() = with(mBinding) {
        dashboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mViewModel.requestMainCardState(position)
            }
        })

        toolbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val maxOffset = appBarLayout.totalScrollRange
            val invOffsetRatio = 1 - abs(verticalOffset.toFloat() / maxOffset)

            val drawable = ResourcesCompat
                .getDrawable(resources, CoreUiRes.drawable.background_bottom_sheet, requireActivity().theme)

            val background = (drawable as LayerDrawable)
                .findDrawableByLayerId(CoreUiRes.id.background) as GradientDrawable
            val dragHandler = drawable
                .findDrawableByLayerId(CoreUiRes.id.dragHandler) as GradientDrawable
            val topPadding = Rect().let {
                background.getPadding(it)
                it.top
            }

            dragHandler.alpha = (ALPHA_MAX_VALUE * invOffsetRatio).toInt()
            background.cornerRadius = invOffsetRatio * getDimen(CoreUiRes.dimen.def_radius)
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                mBinding.dashboardContainer.background = drawable
                mBinding.dashboard.translationY = topPadding * invOffsetRatio
                mIsAppbarExpanded = invOffsetRatio > EXPAND_CHANGE_TRIGGER_OFFSET
            }
        }
    }

    private fun initTitle() = with(mBinding) {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.select_locations -> {
                    mViewModel.goLocationManager()
                    return@setOnMenuItemClickListener true
                }
                R.id.settings -> {
                    mViewModel.goSettings()
                    return@setOnMenuItemClickListener true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        mViewModel.mainCardState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::updateMainCard)
            .launchIn(lifecycleScope)
        mViewModel.locationListState
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach(::updateState)
            .launchIn(lifecycleScope)
    }

    private fun updateMainCard(
        state: MainCardState
    ) = with(mBinding) {
        when (state) {
            is MainCardState.Content -> {
                mainCard.emptyView.hide()
                mainCard.mainTemp.visible()
                mainCard.cityName.visible()
                mainCard.mainTemp.text =
                    getTempString(state.currentTemp)
                mainCard.cityName.text = state.locationName
                updateMainTitle(
                    state.currentTemp,
                    state.locationName,
                )
            }
            MainCardState.Loading -> {
                mainCard.emptyView.loading()
                mainCard.mainTemp.gone()
                mainCard.cityName.gone()
            }
        }
    }

    private fun updateState(state: DashboardState) {
        when (state) {
            is DashboardState.Content -> {
                mBinding.toolbarLayout.visible()
                mBinding.dashboardContainer.visible()
                mBinding.mainEmptyView.hide()
                mDashboardAdapter.submit(state.locations)
            }
            DashboardState.Empty -> {
                mBinding.toolbarLayout.gone()
                mBinding.dashboardContainer.gone()
                mBinding.mainEmptyView.notify(
                    msg = getString(R.string.main_empty_view_msg),
                    buttonMsg = getString(R.string.select_locations),
                    action = mViewModel::goLocationManager
                )
            }
            DashboardState.Error -> {
                mBinding.toolbarLayout.gone()
                mBinding.dashboardContainer.gone()
                mBinding.mainEmptyView.error(
                    msg = getString(CoreUiRes.string.error_msg),
                    buttonText = getString(CoreUiRes.string.refresh),
                    onButtonClick = mViewModel::loadLocations
                )
            }
            DashboardState.Init -> {
                mBinding.toolbarLayout.gone()
                mBinding.dashboardContainer.gone()
                mBinding.mainCard.emptyView.loading()
                mViewModel.loadLocations()
            }
            DashboardState.Loading -> {
                mBinding.toolbarLayout.gone()
                mBinding.dashboardContainer.gone()
                mBinding.mainEmptyView.loading()
                mBinding.mainCard.emptyView.loading()
            }
        }
    }

    private fun updateMainTitle(
        temp: Temp,
        locationName: String
    ) {
        mBinding.collapsingAppbarLayout.title =
            getMainTitle(locationName, temp)
    }

    companion object {
        private const val ALPHA_MAX_VALUE = 255
        private const val EXPAND_CHANGE_TRIGGER_OFFSET = 0.9
    }
}

internal fun Fragment.getMainTitle(
    locationName: String,
    temp: Temp,
): String {
    return when (temp.unitsType) {
        UnitsType.METRIC ->
            getString(R.string.template_celsius_main_title, temp.value.roundToInt(), locationName)
        UnitsType.IMPERIAL ->
            getString(R.string.template_fahrenheit_main_title, temp.value.roundToInt(), locationName)
    }
}
