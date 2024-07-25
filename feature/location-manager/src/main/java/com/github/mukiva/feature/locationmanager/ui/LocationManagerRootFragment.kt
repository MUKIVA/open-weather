package com.github.mukiva.feature.locationmanager.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mukiva.core.ui.component.component
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.feature.locationmanager.R
import com.github.mukiva.feature.locationmanager.databinding.FragmentLocationManagerBinding
import com.github.mukiva.feature.locationmanager.presentation.LocationManagerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class LocationManagerRootFragment : Fragment(R.layout.fragment_location_manager) {

    private val mViewModel by viewModels<LocationManagerViewModel>()
    private val mBinding by viewBindings(FragmentLocationManagerBinding::bind)

    private val mAppBarComponent by component {
        LocationManagerAppbarComponent(
            binding = mBinding.layLocationManagerAppBar,
            onBackPressedDispatcher = { requireActivity().onBackPressedDispatcher },
            onEnterNormalMode = mViewModel::enterNormalMode,
            onNavigateUp = mViewModel::goBack,
            onSelectAll = mViewModel::selectAllEditable,
            onRemoveSelectedLocations = mViewModel::removeSelectedLocations,
            onEnterEditMode = mViewModel::enterEditMode
        )
    }

    private val mSavedLocationsComponent by component {
        SavedLocationListComponent(
            binding = mBinding.laySavedLocationList,
            onEnterEditMode = { location ->
                mViewModel.enterEditMode(location)
            },
            onSelectEditable = mViewModel::switchEditableSelect,
            onItemMove = mViewModel::moveLocation,
            onRetry = mViewModel::retryLoadSavedLocations,
        )
    }

    private val mSearchViewComponent by component {
        SearchViewComponent(
            binding = mBinding.layLocationManagerAppBar,
            onSearchQueryChanged = mViewModel::onSearchQueryChanged,
            onAddLocation = mViewModel::addLocation,
            onRetry = mViewModel::retrySearch,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAppBarComponent.init()
        mAppBarComponent.subscribeOnViewModel(mViewModel, viewLifecycleOwner)

        mSavedLocationsComponent.init()
        mSavedLocationsComponent.subscribeOnViewModel(mViewModel, viewLifecycleOwner)

        mSearchViewComponent.init()
        mSearchViewComponent.subscribeOnViewModel(mViewModel, viewLifecycleOwner)
    }
}
