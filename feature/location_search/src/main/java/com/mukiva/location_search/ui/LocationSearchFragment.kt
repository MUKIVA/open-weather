package com.mukiva.location_search.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mukiva.location_search.R
import com.mukiva.location_search.databinding.FragmentLocationSearchBinding
import com.mukiva.location_search.di.LocationSearchComponent
import com.mukiva.location_search.domain.LocationPoint
import com.mukiva.location_search.presentation.SearchLocationState
import com.mukiva.location_search.presentation.SearchViewModel
import com.mukiva.location_search.ui.adapter.SearchLocationAdapter
import com.mukiva.openweather.ui.emptyView
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.uiLazy
import com.mukiva.openweather.ui.viewBindings
import com.mukiva.openweather.ui.visible
import kotlinx.coroutines.launch

class LocationSearchFragment : Fragment(R.layout.fragment_location_search) {

    private val mViewModel: SearchViewModel by viewModels {
        LocationSearchComponent.get().factory
    }
    private val mBinding by viewBindings(FragmentLocationSearchBinding::bind)
    private val mAdapter by uiLazy { SearchLocationAdapter(
        onAddCallback = {
            mViewModel
        }
    ) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initSearchBar()
        subscribeOnViewModel()

    }

    private fun initSearchBar() = with(mBinding) {
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

        ViewCompat.setOnApplyWindowInsetsListener(list) { view, windowInsets ->

            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val inputInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
            val targetBottomInset = when(inputInsets.bottom) {
                0 -> insets.bottom
                else -> inputInsets.bottom
            }

            ValueAnimator.ofInt(view.paddingBottom, targetBottomInset)
                .apply {
                    duration = 500
                    interpolator = LinearInterpolator()
                    this.addUpdateListener {
                        view.updatePadding(
                            bottom = it.animatedValue as Int
                        )
                    }
                    start()
                }

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun subscribeOnViewModel() {
        lifecycleScope.launch {
            mViewModel.state
                .flowWithLifecycle(lifecycle)
                .collect(::updateState)
        }
    }

    private fun updateState(state: SearchLocationState) = with(mBinding) {
        updateListState(state.listState)
        updateSearch(state.query)
        updateList(state.receivedLocations)
    }

    private fun updateListState(state: SearchLocationState.ListState) = with(mBinding) {
        when(state) {
            SearchLocationState.ListState.ERROR -> {
                list.gone()
                emptyView.error(
                    msg = getString(R.string.search_error),
                    buttonText = getString(R.string.search_error_refresh),
                    onButtonClick = { mViewModel.executeSearch(search.text?.toString() ?: "") }
                )
            }
            SearchLocationState.ListState.EMPTY -> {
                list.gone()
                emptyView.emptyView(
                    msg = getString(R.string.search_empty)
                )
            }
            SearchLocationState.ListState.LOADING -> {
                list.gone()
                emptyView.loading()
            }
            SearchLocationState.ListState.CONTENT -> {
                list.visible()
                emptyView.hide()
            }
        }
    }

    private fun updateSearch(query: String) = with(mBinding) {
        search.setText(query)
        search.setSelection(search.text?.length ?: 0)
    }

    private fun updateList(receivedLocations: List<LocationPoint>) {
        mAdapter.submitList(receivedLocations)
    }

    companion object {
        fun newInstance() = LocationSearchFragment()
    }

}