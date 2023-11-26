package com.mukiva.feature.location_manager_impl.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.core.ui.databinding.LayListStatesBinding
import com.mukiva.feature.location_manager_impl.R
import com.mukiva.feature.location_manager_impl.databinding.FragmentLocationManagerBinding
import com.mukiva.feature.location_manager_impl.di.ILocationManagerComponent
import com.mukiva.feature.location_manager_impl.domain.model.Location
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerEvent
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerState
import com.mukiva.feature.location_manager_impl.presentation.LocationManagerViewModel
import com.mukiva.feature.location_manager_impl.ui.adapter.LocationManagerAdapter
import com.mukiva.openweather.ui.emptyView
import com.mukiva.openweather.ui.error
import com.mukiva.openweather.ui.gone
import com.mukiva.openweather.ui.hide
import com.mukiva.openweather.ui.loading
import com.mukiva.openweather.ui.uiLazy
import com.mukiva.openweather.ui.viewBindings
import com.mukiva.openweather.ui.visible
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LocationManagerFragment : Fragment(R.layout.fragment_location_manager) {

    private val mViewModel: LocationManagerViewModel by viewModels {
        ILocationManagerComponent.get().factory
    }
    private val mBinding by viewBindings(FragmentLocationManagerBinding::bind)
    private val mAddedAdapter by uiLazy { LocationManagerAdapter(
        onAddCallback = {
            mViewModel.addLocation(it)
        }
    )}
    private val mSearchAdapter by uiLazy {
        LocationManagerAdapter(
            onAddCallback = {
                mViewModel.addLocation(it)
            }
        )
    }

    private val mSearchQueryFlow = MutableSharedFlow<String>()


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

        searchBar.contentInsetStartWithNavigation
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
            requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {

                init {
                    searchView.setOnHideListener {
                        remove()
                    }
                }

                override fun handleOnBackPressed() {
                    searchView.hide()
                }

            })
        }

        mSearchQueryFlow
            .debounce(500)
            .onEach { mViewModel.executeSearch(it) }
            .launchIn(lifecycleScope)
    }

    private fun initList() = with(mBinding) {
        addedList.adapter = mAddedAdapter
        searchViewList.adapter = mSearchAdapter

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
        updateListState(
            state = state.addedListState.type,
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
        updateList(state.addedListState.list, mAddedAdapter)
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

    private fun updateList(
        list: List<Location>,
        adapter: LocationManagerAdapter
    ) {
        adapter.submitList(list)
    }

    companion object {
        fun newInstance() = LocationManagerFragment()
    }


}