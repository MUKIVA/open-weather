package com.github.mukiva.feature.dashboard.ui

import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.mukiva.core.ui.error
import com.github.mukiva.core.ui.getDimen
import com.github.mukiva.core.ui.getTempString
import com.github.mukiva.core.ui.gone
import com.github.mukiva.core.ui.hide
import com.github.mukiva.core.ui.loading
import com.github.mukiva.core.ui.notify
import com.github.mukiva.core.ui.uiLazy
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.databinding.FragmentDashboardBinding
import com.github.mukiva.feature.dashboard.presentation.DashboardState
import com.github.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.github.mukiva.feature.dashboard.ui.adapter.DashboardAdapter
import com.github.mukiva.feature.dashboard.ui.adapter.ICurrentWeatherProvider
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

    private val mViewModel by viewModels<DashboardViewModel>()
    private val mBinding by viewBindings(FragmentDashboardBinding::bind)
    private val mDashboardAdapter by uiLazy {
        DashboardAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
    }
    private var mIsAppbarExpanded: Boolean = true
    private var mCurrentPage: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTitle()
        initAppbar()
        initDashboard()
        observeViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_EXPANDED, mIsAppbarExpanded)
        outState.putInt(KEY_CURRENT_PAGE, mCurrentPage)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        mIsAppbarExpanded = savedInstanceState?.getBoolean(KEY_EXPANDED) ?: true
        mCurrentPage = savedInstanceState?.getInt(KEY_CURRENT_PAGE) ?: 0
    }

    private fun initDashboard() = with(mBinding) {
        if (dashboard.adapter == null) {
            dashboard.adapter = mDashboardAdapter
        }
        dashboard.offscreenPageLimit = 2
        dashboard.children.find { child -> child is RecyclerView }?.apply {
            (this as RecyclerView).isNestedScrollingEnabled = false
        }
        toolbarLayout.setExpanded(mIsAppbarExpanded, false)
        dashboard.doOnLayout { dashboard.setCurrentItem(mCurrentPage, false) }
    }

    private fun initAppbar() = with(mBinding) {
        dashboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mCurrentPage = position
                mDashboardAdapter.requestCurrent(position)
                    .flowWithLifecycle(lifecycle)
                    .onEach(::updateMainCard)
                    .launchIn(lifecycleScope)
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
            mBinding.dashboardContainer.background = drawable
            mBinding.dashboard.translationY = topPadding * invOffsetRatio
            mIsAppbarExpanded = invOffsetRatio > EXPAND_CHANGE_TRIGGER_OFFSET
        }
    }

    private fun initTitle() = with(mBinding) {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.select_locations -> {
                    mViewModel.goSelectLocations()
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
        mViewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach(::updateState)
            .launchIn(lifecycleScope)
    }

    private fun updateMainCard(
        current: ICurrentWeatherProvider.Current?
    ) = with(mBinding) {
        when {
            current == null -> {
                mainCard.emptyView.loading()
                mainCard.mainTemp.gone()
                mainCard.cityName.gone()
            }
            else -> {
                mainCard.emptyView.hide()
                mainCard.mainTemp.visible()
                mainCard.cityName.visible()
                mainCard.mainTemp.text =
                    getTempString(current.currentWeather.temp)
                mainCard.cityName.text = current.locationName
                updateMainTitle(
                    current.currentWeather.temp,
                    current.locationName,
                )
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
                    action = mViewModel::goSelectLocations
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

        private const val KEY_EXPANDED = "KEY_EXPANDED"
        private const val KEY_CURRENT_PAGE = "KEY_CURRENT_PAGE"
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
