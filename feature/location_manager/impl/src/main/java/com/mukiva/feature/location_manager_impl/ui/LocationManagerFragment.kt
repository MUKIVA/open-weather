package com.mukiva.feature.location_manager_impl.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.ListAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.core.ui.databinding.LayListStatesBinding
import com.mukiva.feature.location_manager_impl.R
import com.mukiva.feature.location_manager_impl.databinding.FragmentLocationManagerBinding
import com.mukiva.feature.location_manager_impl.presentation.EditableLocation
import com.mukiva.feature.location_manager_impl.presentation.ListState
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerEvent
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerState
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerViewModel
import com.mukiva.feature.location_manager_impl.ui.adapter.DragDropItemTouchHelper
import com.mukiva.feature.location_manager_impl.ui.adapter.LocationManagerSavedAdapter
import com.mukiva.feature.location_manager_impl.ui.adapter.LocationManagerSearchAdapter
import com.mukiva.openweather.ui.emptyView
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.uiLazy
import com.mukiva.openweather.ui.viewBindings
import com.mukiva.openweather.ui.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class VerticalVisibleAnimationWrapper(
    private val viewGroup: ViewGroup
) {
    private val hiddenAnimator get() = ValueAnimator.ofInt(viewGroup.height, HIDDEN_HEIGHT).apply {
        duration = DURATION
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            viewGroup.updateLayoutParams {
                height = it.animatedValue as Int
            }
        }
    }

    private val visibleAnimator get() = ValueAnimator.ofInt(viewGroup.height, VISIBLE_HEIGHT).apply {
        duration = DURATION
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            viewGroup.updateLayoutParams {
                height = it.animatedValue as Int
            }
        }
    }

    fun visible() {
        viewGroup.clearAnimation()
        visibleAnimator.start()
    }

    fun hidden() {
        viewGroup.clearAnimation()
        hiddenAnimator.start()
    }

    companion object {
        private const val DURATION = 250L
        private const val HIDDEN_HEIGHT = 0
        private const val VISIBLE_HEIGHT = 150
    }

}

@AndroidEntryPoint
class LocationManagerFragment : Fragment(R.layout.fragment_location_manager) {

    private val mViewModel by viewModels<LocationManagerViewModel>()
    private val mBinding by viewBindings(FragmentLocationManagerBinding::bind)
    private val mAddedAdapter by uiLazy {
        LocationManagerSavedAdapter(
            onEnterEditMode = { item -> mViewModel.enterEditMode(item) },
            onSelectEditable = { item -> mViewModel.switchEditableSelect(item) },
            onItemRemove =  { item -> mViewModel.removeLocation(item) },
            onItemMoveCallback = { from, to -> mViewModel.moveLocation(from, to) }
        )
    }
    private val mTouchHelperCallback by uiLazy { DragDropItemTouchHelper(mAddedAdapter).apply {
        isEnabled = false
        swipeIsEnabled = false
        dragDropIsEnabled = true
    } }
    private val mTouchHelper by uiLazy { ItemTouchHelper(mTouchHelperCallback) }
    private val mSearchAdapter by uiLazy {
        LocationManagerSearchAdapter(
            onAddCallback = {
                mViewModel.addLocation(it)
            }
        )
    }
    private val mSearchQueryFlow = MutableSharedFlow<String>()
    private val mOnBackPressedDecorator = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            mViewModel.enterNormalMode()
        }
    }
    private val mSearchBarAnimator by uiLazy {
        VerticalVisibleAnimationWrapper(mBinding.searchBar)
    }
    private val mToolBarAnimator by uiLazy {
        VerticalVisibleAnimationWrapper(mBinding.toolbar)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActionBar()
        initList()
        initSearchView()

        subscribeOnViewModel()
    }

    private fun initActionBar() = with(mBinding) {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(searchBar)

        toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId) {
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
            mViewModel.enterSearchMode()
        }
        searchView.setOnHideListener {
            mOnBackPressedDecorator.remove()
        }

        mSearchQueryFlow
            .debounce(500)
            .onEach { mViewModel.executeSearch(it) }
            .launchIn(lifecycleScope)
    }

    private fun initList() = with(mBinding) {
        addedList.adapter = mAddedAdapter
        searchViewList.adapter = mSearchAdapter

        mTouchHelper.attachToRecyclerView(addedList)


        ViewCompat.setOnApplyWindowInsetsListener(appbar) { v, insets ->
            val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())

            v.updatePadding(
                bottom = statusBars.bottom,
                top = statusBars.top,
                right = statusBars.right,
                left = statusBars.left
            )

            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(searchView) { v, insets ->
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())

            v.updatePadding(
                top = ime.top,
                bottom = ime.bottom,
                left = ime.left,
                right = ime.right
            )

            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(addedList) { v, insets ->
            val nav = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            v.updatePadding(
                top = nav.top,
                bottom = nav.bottom
            )

            insets
        }
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
        updateMode(state.type)
        updateListState(
            state = state.savedListState.type,
            list = addedList,
            emptyView = addedEmptyView,
            onFailBind = {
                mBinding.addedEmptyView.error(
                    msg = getString(R.string.search_error),
                    buttonText = getString(R.string.search_error_refresh),
                    onButtonClick = { mViewModel.executeSearch("") }
                )
            }

        )
        updateListState(
            state = state.searchListState.type,
            list = searchViewList,
            emptyView = searchEmptyView,
            onFailBind = {
                mBinding.searchEmptyView.error(
                    msg = getString(R.string.search_error),
                    buttonText = getString(R.string.search_error_refresh),
                    onButtonClick = { mViewModel.executeSearch("") }
                )
            }
        )
        updateList(state.searchListState.list, mSearchAdapter)
        updateList(state.savedListState.list, mAddedAdapter)
        updateEditTitle(state.savedListState)
    }
    private fun updateEditTitle(state: ListState<EditableLocation>) {
        with(EditableLocation) {
            mBinding.toolbar.title = getString(
                R.string.selected_location_count,
                state.selectedCount
            )
        }
    }
    private fun updateListState(
        state: LocationManagerState.ListStateType,
        list: RecyclerView,
        emptyView: LayListStatesBinding,
        onFailBind: () -> Unit
    ) {
        when(state) {
            LocationManagerState.ListStateType.ERROR -> {
                list.gone()
                onFailBind()
            }
            LocationManagerState.ListStateType.EMPTY -> {
                list.gone()
                emptyView.emptyView(
                    msg = getString(R.string.search_empty)
                )
            }
            LocationManagerState.ListStateType.LOADING -> {
                list.gone()
                emptyView.loading()
            }
            LocationManagerState.ListStateType.CONTENT -> {
                list.visible()
                emptyView.hide()
            }
        }
    }

    private fun updateMode(mode: LocationManagerState.Type) {
        when(mode) {
            LocationManagerState.Type.NORMAL -> with(mBinding) {
                mOnBackPressedDecorator.remove()
                mToolBarAnimator.hidden()
                mSearchBarAnimator.visible()
                searchView.hide()
                searchView.animate()
                mTouchHelperCallback.isEnabled = false
            }
            LocationManagerState.Type.EDIT -> with(mBinding) {
//                toolbar.visible()
                mToolBarAnimator.visible()
//                searchBar.gone()
                mSearchBarAnimator.hidden()
                searchView.hide()
                requireActivity().onBackPressedDispatcher
                    .addCallback(mOnBackPressedDecorator)
                mTouchHelperCallback.isEnabled = true


            }
            LocationManagerState.Type.SEARCH -> {
                requireActivity().onBackPressedDispatcher
                    .addCallback(mOnBackPressedDecorator)
                mTouchHelperCallback.isEnabled = false
            }
        }
    }

    private fun <T> updateList(
        list: List<T>,
        adapter: ListAdapter<T, *>
    ) {
        adapter.submitList(list)
    }

    companion object {
        fun newInstance() = LocationManagerFragment()
    }


}