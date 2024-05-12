package com.mukiva.feature.dashboard.ui

import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.mukiva.open_weather.core.domain.Temp
import com.github.mukiva.open_weather.core.domain.UnitsType
import com.mukiva.core.ui.getDimen
import com.mukiva.core.ui.getTempString
import com.mukiva.feature.dashboard.R
import com.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.mukiva.feature.dashboard.ui.adapter.DashboardAdapter
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.notify
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.feature.dashboard.presentation.DashboardState
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.abs
import com.mukiva.feature.dashboard.databinding.FragmentDashboardBinding
import com.mukiva.feature.dashboard.ui.adapter.ICurrentWeatherProvider
import kotlin.math.roundToInt
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

    override fun onStop() = with(mBinding) {
        super.onStop()
        dashboard.adapter = null
    }

    override fun onResume() = with(mBinding) {
        super.onResume()
        dashboard.adapter = mDashboardAdapter
    }

    private fun initDashboard() = with(mBinding) {
        dashboard.offscreenPageLimit = 2
        dashboard.children.find { child -> child is RecyclerView }?.apply {
            (this as RecyclerView).isNestedScrollingEnabled = false
        }
    }

    private fun initAppbar() = with(mBinding) {

        dashboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
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
                .getDrawable(resources, R.drawable.background_dashboard, requireActivity().theme)

            val background = (drawable as LayerDrawable)
                .findDrawableByLayerId(R.id.background) as GradientDrawable
            val dragHandler = drawable
                .findDrawableByLayerId(R.id.dragHandler) as GradientDrawable
            val topPadding = Rect().let { background.getPadding(it); it.top }

            dragHandler.alpha = (ALPHA_MAX_VALUE * invOffsetRatio).toInt()
            background.cornerRadius = invOffsetRatio * getDimen(CoreUiRes.dimen.def_radius)
            mBinding.dashboardContainer.background = drawable
            mBinding.dashboard.translationY = topPadding * invOffsetRatio
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
        when(state) {
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
                    msg = "TODO(ADD LOCATION)",
                    buttonMsg = "TODO(Add)",
                    action = mViewModel::goSelectLocations
                )
            }
            DashboardState.Error -> {
                mBinding.toolbarLayout.gone()
                mBinding.dashboardContainer.gone()
                mBinding.mainEmptyView.error(
                    msg = "TODO(ERROR)",
                    buttonText = "TODO(REFRESH)",
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
    }
}

internal fun Fragment.getMainTitle(
    locationName: String,
    temp: Temp,
): String {
    return when(temp.unitsType) {
        UnitsType.METRIC ->
            getString(R.string.template_celsius_main_title, temp.value.roundToInt(), locationName)
        UnitsType.IMPERIAL ->
            getString(R.string.template_fahrenheit_main_title, temp.value.roundToInt(), locationName)
    }
}