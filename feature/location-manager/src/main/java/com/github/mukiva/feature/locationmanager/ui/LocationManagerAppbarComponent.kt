package com.github.mukiva.feature.locationmanager.ui

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.component.Component
import com.github.mukiva.core.ui.gone
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.locationmanager.R
import com.github.mukiva.feature.locationmanager.databinding.LayLocationManagerAppbarBinding
import com.github.mukiva.feature.locationmanager.presentation.ILocationManagerAppbarState
import com.github.mukiva.feature.locationmanager.presentation.LocationManagerViewModel
import com.google.android.material.search.SearchView.TransitionState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class LocationManagerAppbarComponent(
    private val binding: LayLocationManagerAppbarBinding,
    private val onBackPressedDispatcher: () -> OnBackPressedDispatcher,
    private val onEnterNormalMode: () -> Unit,
    private val onEnterEditMode: () -> Unit,
    private val onNavigateUp: () -> Unit,
    private val onSelectAll: () -> Unit,
    private val onRemoveSelectedLocations: () -> Unit,
) : Component(), Component.IStateObserver<LocationManagerViewModel> {

    private val mSearchOnBackPressedDecorator = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            binding.searchView.hide()
        }
    }

    private val mEditOnBackPressedDispatcher = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onEnterNormalMode()
            remove()
        }
    }

    override fun init() {
        initSearchBar()
        initListeners()
        initInsets()
    }

    override fun subscribeOnViewModel(
        viewModel: LocationManagerViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModel.appBarStateFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .onEach(::onStateUpdate)
            .launchIn(lifecycleOwner.lifecycleScope)
    }

    private fun onStateUpdate(state: ILocationManagerAppbarState) = with(binding) {
        when (state) {
            ILocationManagerAppbarState.Edit -> setEditState()
            ILocationManagerAppbarState.Normal -> setNormalState()
        }
    }

    private fun initListeners() = with(binding) {
        searchBar.setNavigationOnClickListener {
            onNavigateUp()
        }

        searchView.addTransitionListener { _, _, newState ->
            when (newState) {
                TransitionState.SHOWING -> onBackPressedDispatcher()
                    .addCallback(mSearchOnBackPressedDecorator)
                TransitionState.HIDING -> mSearchOnBackPressedDecorator.remove()
                else -> {}
            }
        }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.remove -> {
                    onRemoveSelectedLocations()
                    true
                }
                R.id.selectAll -> {
                    onSelectAll()
                    true
                }
                else -> false
            }
        }
        toolbar.setNavigationOnClickListener {
            onEnterNormalMode()
        }
    }

    private fun initInsets() = with(binding) {
        ViewCompat.setOnApplyWindowInsetsListener(appbarContainer) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(
                left = statusBarInsets.left + navInsets.left,
                right = statusBarInsets.right + navInsets.right,
                top = statusBarInsets.top,
                bottom = statusBarInsets.bottom
            )
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(searchViewList) { v, insets ->
            val navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(
                left = navInsets.left,
                right = navInsets.right
            )
            insets
        }
    }

    private fun LayLocationManagerAppbarBinding.setEditState() {
        searchBar.gone()
        toolbar.visible()
        onBackPressedDispatcher()
            .addCallback(mEditOnBackPressedDispatcher)
    }

    private fun initSearchBar() = with(binding) {
        searchBar.inflateMenu(R.menu.view_locations_menu)
        searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    onEnterEditMode()
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
    }
}

internal fun LayLocationManagerAppbarBinding.setNormalState() {
    searchBar.visible()
    toolbar.visible()
}
