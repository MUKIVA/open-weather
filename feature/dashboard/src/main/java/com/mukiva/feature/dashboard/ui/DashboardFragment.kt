package com.mukiva.feature.dashboard.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.mukiva.core.ui.collectWithScope
import com.mukiva.core.ui.getDimen
import com.mukiva.core.ui.getInteger
import com.mukiva.feature.dashboard.R
import com.mukiva.feature.dashboard.databinding.FragmentDashboardBinding
import com.mukiva.feature.dashboard.domain.model.UnitsType
import com.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.mukiva.feature.dashboard.ui.adapter.DashboardAdapter
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.notify
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
import com.mukiva.feature.dashboard.presentation.IDashboardState
import com.mukiva.openweather.ui.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs
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

    override fun onResume() = with(mBinding) {
        super.onResume()
        dashboard.adapter = mDashboardAdapter
    }

    override fun onStop() = with(mBinding) {
        super.onStop()
        dashboard.adapter = null
    }

    private fun initDashboard() = with(mBinding) {
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

        toolbarLayout.setExpanded(mViewModel.toolbarIsExpanded, false)
        mainCard.root.clipToPadding = false

        toolbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val maxOffset = appBarLayout.totalScrollRange
            val invOffsetRatio = 1 - abs(verticalOffset.toFloat() / maxOffset)
            val defRadius = getDimen(CoreUiRes.dimen.def_radius)
            val maxElevation = getDimen(CoreUiRes.dimen.def_max_elevation)

            val radius = invOffsetRatio * defRadius
            val elevation = invOffsetRatio * invOffsetRatio * maxElevation
            mainCard.card.cardElevation = elevation
            dashboardContainer.radius = radius

            mViewModel.toolbarIsExpanded = maxOffset != abs(verticalOffset)
        }
    }

    private fun initTitle() = with(mBinding) {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.select_locations -> {
                    mViewModel.goSelectLocations()
                    return@setOnMenuItemClickListener false
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
        with(mViewModel) {
            observeState(IDashboardState.ScreenType::class, viewLifecycleOwner) {
                updateType(it.type)
            }
            observeState(IDashboardState.MinorState::class, viewLifecycleOwner) {
                updateDashboard(it.list.size)
            }
            observeState(IDashboardState.MainCardState::class, viewLifecycleOwner) {
                updateMainCard(it, it.unitsType)
            }
        }
        mViewModel.toolbarIsExpandedFlow
            .flowWithLifecycle(lifecycle)
            .collectWithScope(lifecycleScope) {
                val duration = getInteger(CoreUiRes.integer.def_animation_duration).toLong()
                val handlerVerticalMargin = getDimen(com.mukiva.core.ui.R.dimen.def_v_padding)
                val dragHandlerHeight = mBinding.dragHandler.height
                val translate = handlerVerticalMargin * 2 + dragHandlerHeight
                when(it) {
                    true -> {
                        mBinding.dragHandler
                            .animate()
                            .setDuration(duration)
                            .alpha(MAX_DRAG_HANDLER_ALPHA)
                        mBinding.dashboard
                            .animate()
                            .setDuration(duration)
                            .translationY(translate.toFloat())
                    }
                    false -> {
                        mBinding.dragHandler
                            .animate()
                            .setDuration(duration)
                            .alpha(MIN_DRAG_HANDLER_ALPHA)
                        mBinding.dashboard
                            .animate()
                            .setDuration(duration)
                            .translationY(0.0f)
                    }
                }
            }
    }

    private fun updateDashboard(count: Int) = with(mBinding) {
        if (count != 0) {
            mDashboardAdapter.submit(count)
            if (mBinding.dashboard.adapter == null)
                mBinding.dashboard.adapter = mDashboardAdapter
            dashboard.offscreenPageLimit = count.coerceIn(1, count)
        }
    }

    private fun updateMainCard(state: IDashboardState.MainCardState, unitsType: UnitsType) = with(mBinding) {

        val mainCardTemp: (UnitsType) -> String = { unitsType ->
            when(unitsType) {
                UnitsType.METRIC -> getString(CoreUiRes.string.template_celsius, state.tempC)
                UnitsType.IMPERIAL -> getString(CoreUiRes.string.template_fahrenheit, state.tempF)
            }
        }

        val createTitle: (UnitsType) -> String = { unitsType ->
            when(unitsType) {
                UnitsType.METRIC -> getString(
                    R.string.template_celsius_main_title,
                    state.tempC, state.cityName
                )
                UnitsType.IMPERIAL -> getString(
                    R.string.template_fahrenheit_main_title,
                    state.tempF, state.cityName
                )
            }
        }

        val updateMainCardType: (IDashboardState.MainCardState.Type) -> Unit = {
            when(it) {
                IDashboardState.MainCardState.Type.LOADING -> {
                    mainCard.cityName.gone()
                    mainCard.mainTemp.gone()
                    mainCard.emptyView.loading()
                }
                IDashboardState.MainCardState.Type.CONTENT -> {
                    mainCard.cityName.visible()
                    mainCard.mainTemp.visible()
                    mainCard.emptyView.hide()
                }
            }
        }

        updateMainCardType(state.type)
        if (state.type == IDashboardState.MainCardState.Type.LOADING) return

        val title = createTitle(unitsType)

        toolbar.title = title
        collapsingAppbarLayout.title = title
        mainCard.mainTemp.text = mainCardTemp(unitsType)
        mainCard.cityName.text = state.cityName
    }

    private fun updateType(type: IDashboardState.ScreenType.Type) = with(mBinding) {

        val setContentVisible: (View.() -> Unit) -> Unit = {
            toolbarLayout.it()
            dashboardContainer.it()
        }
        when(type) {
            IDashboardState.ScreenType.Type.INIT -> {
                mainEmptyView.loading()
                setContentVisible(View::gone)
                mViewModel.load()
            }
            IDashboardState.ScreenType.Type.LOADING -> {
                mainEmptyView.loading()
                setContentVisible(View::gone)
            }
            IDashboardState.ScreenType.Type.CONTENT -> {
                mainEmptyView.hide()
                setContentVisible(View::visible)
            }
            IDashboardState.ScreenType.Type.ERROR -> {
                mainEmptyView.error("TODO: ADD ERROR TEXT", "TODO: REFRESH") {
                    mViewModel.load()
                }
                setContentVisible(View::gone)
            }
            IDashboardState.ScreenType.Type.EMPTY -> {
                mainEmptyView.notify("Please, add your city", "Search") { mViewModel.goSelectLocations() }
                setContentVisible(View::gone)
            }
        }
    }

    private fun Fragment.ifResumed(block: () -> Unit) {
        if (lifecycle.currentState == Lifecycle.State.RESUMED) {
            block()
        }
    }

    companion object {

        private const val MAX_DRAG_HANDLER_ALPHA = 0.5f
        private const val MIN_DRAG_HANDLER_ALPHA = 0.0f
        fun newInstance() = DashboardFragment()
    }
}