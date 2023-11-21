package com.mukiva.feature.location_manager_impl.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mukiva.feature.location_manager_impl.R
import com.mukiva.feature.location_manager_impl.databinding.FragmentLocationManagerBinding
import com.mukiva.feature.location_manager_impl.di.LocationManagerComponent
import com.mukiva.feature.location_manager_impl.domain.model.Location
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerEvent
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerState
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerViewModel
import com.mukiva.feature.location_manager_impl.ui.adapter.LocationManagerAdapter
import com.mukiva.openweather.ui.CollapsingToolbarStateListener
import com.mukiva.openweather.ui.emptyView
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.uiLazy
import com.mukiva.openweather.ui.viewBindings
import com.mukiva.openweather.ui.visible
import kotlinx.coroutines.launch

class LocationManagerFragment : Fragment(R.layout.fragment_location_manager) {

    private val mViewModel: LocationManagerViewModel by viewModels {
        LocationManagerComponent.get().factory
    }
    private val mBinding by viewBindings(FragmentLocationManagerBinding::bind)
    private val mAdapter by uiLazy { LocationManagerAdapter(
        onAddCallback = {
            mViewModel.addLocation(it)
        }
    )}
    private val mEmptyViewAnimator by uiLazy {
        EmptyViewAnimator(
            viewGroup = mBinding.emptyView.root,
            expandBottomPadding = mBinding.appbar.totalScrollRange,
            collapsingBottomPadding = 0
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEmptyView()
        initList()
        initSearchBar()
        initCollapsingToolbarLayout()
        subscribeOnViewModel()
    }

    private fun initEmptyView() = with(mBinding) {

        appbar.addOnOffsetChangedListener(object : CollapsingToolbarStateListener() {
            override fun onStateChanged(state: State) {
                Log.d("addOnOffsetChangedListener", "$state")
                when (state) {
                    State.COLLAPSED -> mEmptyViewAnimator.collapse()
                    State.EXPANDED -> mEmptyViewAnimator.expand()
                    else -> {}
                }
            }
        })
    }

    private fun initCollapsingToolbarLayout() = with(mBinding) {
        collapsingToolbar.titlePositionInterpolator = DecelerateInterpolator()
        collapsingToolbar.titleCollapseMode = CollapsingToolbarLayout.TITLE_COLLAPSE_MODE_SCALE
    }

    private fun initSearchBar() = with(mBinding) {
        clearButton.setOnClickListener {
            mViewModel.clearQuery()
            list.scrollToPosition(0)
            appbar.setExpanded(true)
        }
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mViewModel.updateSearchQuery(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun initList() = with(mBinding) {
        list.adapter = mAdapter
    }

    private fun subscribeOnViewModel() {
        lifecycleScope.launch {
            mViewModel.state
                .flowWithLifecycle(lifecycle)
                .collect(::updateState)
        }
        mViewModel.subscribeOnEvent(::handleEvent)
    }

    private fun handleEvent(evt: LocationManagerEvent) {
        when(evt) {
            is LocationManagerEvent.Toast -> sendToast(evt.msg)
        }
    }

    private fun sendToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    private fun updateState(state: LocationManagerState) = with(mBinding) {
        updateListState(state.listState)
        updateSearch(state.query)
        updateList(state.receivedLocations)
    }

    private fun updateListState(state: LocationManagerState.ListState) = with(mBinding) {
        when(state) {
            LocationManagerState.ListState.ERROR -> {
                list.gone()
                emptyView.error(
                    msg = getString(R.string.search_error),
                    buttonText = getString(R.string.search_error_refresh),
                    onButtonClick = { mViewModel.executeSearch(search.text?.toString() ?: "") }
                )
            }
            LocationManagerState.ListState.EMPTY -> {
                list.gone()
                emptyView.emptyView(
                    msg = getString(R.string.search_empty)
                )
            }
            LocationManagerState.ListState.LOADING -> {
                list.gone()
                emptyView.loading()
            }
            LocationManagerState.ListState.CONTENT -> {
                list.visible()
                emptyView.hide()
            }
        }
    }

    private fun updateSearch(query: String) = with(mBinding) {
        search.setText(query)
        search.setSelection(search.text?.length ?: 0)
        setClearButtonVisible(query.isNotEmpty())
    }

    private fun setClearButtonVisible(isVisible: Boolean) = with(mBinding) {
        clearButton.isVisible = isVisible
    }

    private fun updateList(receivedLocations: List<Location>) {
        mAdapter.submitList(receivedLocations)
    }

}