package com.github.mukiva.feature.locationmanager.ui

import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.component.Component
import com.github.mukiva.core.ui.emptyView
import com.github.mukiva.core.ui.error
import com.github.mukiva.core.ui.gone
import com.github.mukiva.core.ui.loading
import com.github.mukiva.feature.locationmanager.R
import com.github.mukiva.feature.locationmanager.databinding.LayLocationManagerAppbarBinding
import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.feature.locationmanager.presentation.ISearchLocationsState
import com.github.mukiva.feature.locationmanager.presentation.LocationManagerViewModel
import com.github.mukiva.feature.locationmanager.ui.adapter.LocationManagerSearchAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.github.mukiva.core.ui.R as CoreUiRes

internal class SearchViewComponent(
    private val binding: LayLocationManagerAppbarBinding,
    private val onSearchQueryChanged: (text: String?) -> Unit,
    private val onAddLocation: (location: Location) -> Unit,
    private val onRetry: () -> Unit
) : Component(), Component.IStateObserver<LocationManagerViewModel> {

    private val mSearchViewListAdapter by lazy {
        LocationManagerSearchAdapter(
            onAddCallback = { location ->
                onAddLocation(location)
                binding.searchView.hide()
            }
        )
    }

    override fun init() {
        initSearchView()
        initSearchViewList()
    }

    override fun subscribeOnViewModel(
        viewModel: LocationManagerViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModel.searchLocationState
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .onEach(::onStateUpdate)
            .launchIn(lifecycleOwner.lifecycleScope)
    }

    private fun onStateUpdate(state: ISearchLocationsState) = with(binding) {
        when (state) {
            is ISearchLocationsState.Content -> setContentState(state)
            ISearchLocationsState.Error -> setErrorState(onRetry)
            ISearchLocationsState.Loading -> setLoadingState()
        }
    }

    private fun initSearchView() = with(binding) {
        searchView
            .editText
            .addTextChangedListener { text -> onSearchQueryChanged(text.toString()) }
    }

    private fun initSearchViewList() = with(binding) {
        searchViewList.adapter = mSearchViewListAdapter
    }

    private fun LayLocationManagerAppbarBinding.setContentState(
        state: ISearchLocationsState.Content
    ) {
        searchEmptyView.emptyView(root.context.getString(R.string.search_empty))
        searchEmptyView.root.isVisible = state.data.isEmpty()
        searchViewList.isVisible = state.data.isNotEmpty()
        mSearchViewListAdapter.submitList(state.data)
    }
}

internal fun LayLocationManagerAppbarBinding.setErrorState(onRetry: () -> Unit) {
    searchViewList.gone()
    searchEmptyView.error(
        msg = root.context.getString(CoreUiRes.string.error_msg),
        buttonText = root.context.getString(CoreUiRes.string.refresh),
        onButtonClick = onRetry
    )
}

internal fun LayLocationManagerAppbarBinding.setLoadingState() {
    searchViewList.gone()
    searchEmptyView.loading()
}
