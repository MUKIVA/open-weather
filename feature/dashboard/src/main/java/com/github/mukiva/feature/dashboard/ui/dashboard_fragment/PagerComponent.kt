package com.github.mukiva.feature.dashboard.ui.dashboard_fragment

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.mukiva.core.ui.component.Component
import com.github.mukiva.core.ui.error
import com.github.mukiva.core.ui.gone
import com.github.mukiva.core.ui.hide
import com.github.mukiva.core.ui.loading
import com.github.mukiva.core.ui.notify
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.dashboard.R
import com.github.mukiva.feature.dashboard.databinding.FragmentDashboardBinding
import com.github.mukiva.feature.dashboard.presentation.DashboardViewModel
import com.github.mukiva.feature.dashboard.presentation.IDashboardState
import com.github.mukiva.feature.dashboard.ui.adapter.DashboardAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.github.mukiva.core.ui.R as CoreUiRes

internal class PagerComponent(
    private val binding: FragmentDashboardBinding,
    private val fragmentManager: FragmentManager,
    private val lifecycle: Lifecycle,
    private val onPageChanged: (Int) -> Unit,
    private val onEmptyAction: () -> Unit,
    private val onErrorAction: () -> Unit,
) : Component(), Component.IStateObserver<DashboardViewModel> {

    private val mAdapter by lazy {
        DashboardAdapter(
            fm = fragmentManager,
            lifecycle = lifecycle
        )
    }

    override fun init(): Unit = with(binding) {
        viewPager.adapter = mAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                onPageChanged.invoke(position)
            }
        })
        viewPager.offscreenPageLimit = 2
        viewPager.children.find { child -> child is RecyclerView }?.apply {
            (this as RecyclerView).isNestedScrollingEnabled = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(viewPager) { _, insets ->
            val navigationInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            viewPager.updatePadding(
                bottom = navigationInsets.bottom
            )

            insets
        }
    }

    override fun subscribeOnViewModel(
        viewModel: DashboardViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModel.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach(::onUpdateState)
            .launchIn(lifecycleOwner.lifecycleScope)
    }

    private fun onUpdateState(state: IDashboardState) {
        when (state) {
            is IDashboardState.Content -> setContentState(state)
            IDashboardState.Empty -> setEmptyState()
            IDashboardState.Error -> setErrorState()
            IDashboardState.Loading -> setLoadingState()
        }
    }

    private fun setEmptyState() = with(binding) {
        layActionBar.root.gone()
        scrollView.gone()
        mainEmptyView.notify(
            msg = root.context.getString(R.string.main_empty_view_msg),
            buttonMsg = root.context.getString(R.string.select_locations),
            action = onEmptyAction
        )
    }

    private fun setErrorState() = with(binding) {
        layActionBar.root.gone()
        scrollView.gone()
        mainEmptyView.error(
            msg = root.context.getString(CoreUiRes.string.error_msg),
            buttonText = root.context.getString(CoreUiRes.string.refresh),
            onButtonClick = onErrorAction
        )
    }

    private fun setLoadingState() = with(binding) {
        layActionBar.root.gone()
        scrollView.gone()
        mainEmptyView.loading()
    }

    private fun setContentState(data: IDashboardState.Content) = with(binding) {
        mainEmptyView.hide()
        layActionBar.root.visible()
        scrollView.visible()
        mAdapter.submitList(data.forecasts)
    }
}