package com.github.mukiva.feature.locationmanager.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.mukiva.core.ui.emptyView
import com.github.mukiva.core.ui.error
import com.github.mukiva.core.ui.getDimen
import com.github.mukiva.core.ui.getInteger
import com.github.mukiva.core.ui.gone
import com.github.mukiva.core.ui.hide
import com.github.mukiva.core.ui.loading
import com.github.mukiva.core.ui.uiLazy
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.locationmanager.R
import com.github.mukiva.feature.locationmanager.databinding.FragmentLocationManagerBinding
import com.github.mukiva.feature.locationmanager.presentation.LocationManagerViewModel
import com.github.mukiva.feature.locationmanager.presentation.SavedLocationsState
import com.github.mukiva.feature.locationmanager.presentation.SearchLocationsState
import com.github.mukiva.feature.locationmanager.ui.adapter.DragDropItemTouchHelper
import com.github.mukiva.feature.locationmanager.ui.adapter.LocationManagerSavedAdapter
import com.github.mukiva.feature.locationmanager.ui.adapter.LocationManagerSearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.github.mukiva.core.ui.R as CoreUiRes

@AndroidEntryPoint
class LocationManagerFragment : Fragment(R.layout.fragment_location_manager) {

    private val mViewModel by viewModels<LocationManagerViewModel>()
    private val mBinding by viewBindings(FragmentLocationManagerBinding::bind)
    private val mAddedAdapter by uiLazy {
        LocationManagerSavedAdapter(
            onEnterEditMode = { item -> mViewModel.enterEditMode(item) },
            onSelectEditable = { item -> mViewModel.switchEditableSelect(item) },
            onItemRemove = { item -> mViewModel.removeLocation(item) },
            onItemMoveCallback = { from, to -> mViewModel.moveLocation(from, to) }
        )
    }
    private val mTouchHelperCallback by uiLazy {
        DragDropItemTouchHelper(mAddedAdapter).apply {
            isEnabled = false
            swipeIsEnabled = false
            dragDropIsEnabled = true
        }
    }

    private val mSearchViewBackgroundColorAnimator by uiLazy {
        ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, Color.BLACK).apply {
            duration = getInteger(CoreUiRes.integer.def_animation_duration).toLong()
            addUpdateListener {
                val color = it.animatedValue as Int
                mBinding.searchView.setBackgroundColor(color)
            }
        }
    }

    private val mTouchHelper by uiLazy { ItemTouchHelper(mTouchHelperCallback) }
    private val mSearchAdapter by uiLazy {
        LocationManagerSearchAdapter(
            onAddCallback = {
                mViewModel.addLocation(it)
            }
        )
    }
    private val mSearchQueryFlow = MutableSharedFlow<String>()

    private val mSearchOnBackPressedDecorator = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            mBinding.searchView.hide()
        }
    }
    private val mEditOnBackPressedDecorator = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            mViewModel.enterNormalMode()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActionBar()
        initList()
        initSearchView()

        subscribeOnViewModel()
    }

    private fun initActionBar() = with(mBinding) {
        searchBar.setNavigationOnClickListener { mViewModel.goBack() }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.remove -> {
                    mViewModel.removeSelectedLocations()
                    true
                }
                R.id.selectAll -> {
                    mViewModel.selectAllEditable()
                    true
                }
                else -> false
            }
        }

        toolbar.setNavigationOnClickListener {
            mViewModel.enterNormalMode()
        }
    }

    @OptIn(FlowPreview::class)
    private fun initSearchView() = with(mBinding) {
        searchView
            .editText
            .addTextChangedListener {
                lifecycleScope.launch {
                    searchBar.setText(searchView.text)
                    mSearchQueryFlow.emit(searchView.text.toString())
                }
            }

        searchView.setOnShowListener {
            mSearchViewBackgroundColorAnimator.start()
            requireActivity().onBackPressedDispatcher
                .addCallback(mSearchOnBackPressedDecorator)
        }
        searchView.setOnHideListener {
            mSearchViewBackgroundColorAnimator.reverse()
            mSearchOnBackPressedDecorator.remove()
        }

        mSearchQueryFlow
            .debounce(DEBOUNCE_EDIT_TEXT)
            .onEach { mViewModel.executeSearch(it) }
            .launchIn(lifecycleScope)
    }

    private fun initList() = with(mBinding) {
        addedList.adapter = mAddedAdapter
        searchViewList.adapter = mSearchAdapter

        mTouchHelper.attachToRecyclerView(addedList)

        ViewCompat.setOnApplyWindowInsetsListener(searchView) { v, insets ->
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.updatePadding(bottom = ime.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(addedList) { v, insets ->
            val nav = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(bottom = nav.bottom)
            insets
        }

        addedList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = getDimen(R.dimen.def_v_padding)
            }
        })
    }

    private fun subscribeOnViewModel() {
        mViewModel.savedLocationsState
            .flowWithLifecycle(lifecycle)
            .onEach(::updateSavedLocationsState)
            .launchIn(lifecycleScope)
        mViewModel.searchLocationState
            .flowWithLifecycle(lifecycle)
            .onEach(::updateSearchLocationsState)
            .launchIn(lifecycleScope)
    }

    private fun updateSavedLocationsState(state: SavedLocationsState) {
        when (state) {
            is SavedLocationsState.Content -> with(mBinding) {
                addedEmptyView.hide()
                addedList.visible()
                mTouchHelperCallback.isEnabled = false
                toolbar.animate()
                    .alpha(0.0f)
                searchBar.animate()
                    .alpha(1.0f)
                    .withStartAction { searchBar.visible() }
                mEditOnBackPressedDecorator.remove()
                mAddedAdapter.submitList(state.data)
            }
            SavedLocationsState.Empty -> with(mBinding) {
                addedList.gone()
                addedEmptyView.emptyView(getString(R.string.saved_locatios_empty_view))
                toolbar.animate()
                    .alpha(0.0f)
                searchBar.animate()
                    .alpha(1.0f)
                    .withStartAction { searchBar.visible() }
                mEditOnBackPressedDecorator.remove()
            }
            SavedLocationsState.Error -> with(mBinding) {
                addedList.gone()
                addedEmptyView.error(
                    msg = getString(CoreUiRes.string.error_msg),
                    buttonText = getString(CoreUiRes.string.refresh),
                    onButtonClick = mViewModel::fetchAddedLocations
                )
            }
            SavedLocationsState.Init -> with(mBinding) {
                addedList.gone()
                addedEmptyView.loading()
                mViewModel.fetchAddedLocations()
            }
            SavedLocationsState.Loading -> with(mBinding) {
                addedList.gone()
                addedEmptyView.loading()
            }
            is SavedLocationsState.Edit -> with(mBinding) {
                addedEmptyView.hide()
                addedList.visible()
                mTouchHelperCallback.isEnabled = true
                requireActivity().onBackPressedDispatcher
                    .addCallback(mEditOnBackPressedDecorator)
                toolbar.animate().alpha(1.0f)
                searchBar.animate()
                    .alpha(0.0f)
                    .withEndAction { searchBar.gone() }
                mAddedAdapter.submitList(state.data)
            }
        }
    }

    private fun updateSearchLocationsState(state: SearchLocationsState) {
        when (state) {
            is SearchLocationsState.Content -> with(mBinding) {
                searchEmptyView.hide()
                searchViewList.visible()
                mSearchAdapter.submitList(state.data)
            }
            SearchLocationsState.Empty -> with(mBinding) {
                searchEmptyView.emptyView(getString(R.string.search_empty))
                searchViewList.gone()
            }
            SearchLocationsState.Error -> with(mBinding) {
                searchEmptyView.error(
                    msg = getString(CoreUiRes.string.error_msg),
                    buttonText = getString(CoreUiRes.string.refresh),
                    onButtonClick = { mViewModel.executeSearch(searchView.text.toString()) }
                )
                searchViewList.gone()
            }
            SearchLocationsState.Loading -> with(mBinding) {
                searchEmptyView.loading()
                searchViewList.gone()
            }
        }
    }

    companion object {
        private const val DEBOUNCE_EDIT_TEXT = 500L
    }
}
