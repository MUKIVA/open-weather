package com.github.mukiva.feature.locationmanager.ui

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.mukiva.core.ui.component.Component
import com.github.mukiva.core.ui.emptyView
import com.github.mukiva.core.ui.error
import com.github.mukiva.core.ui.loading
import com.github.mukiva.feature.locationmanager.R
import com.github.mukiva.feature.locationmanager.databinding.LaySavedLocationListBinding
import com.github.mukiva.feature.locationmanager.presentation.EditableLocation
import com.github.mukiva.feature.locationmanager.presentation.ISavedLocationsState
import com.github.mukiva.feature.locationmanager.presentation.LocationManagerViewModel
import com.github.mukiva.feature.locationmanager.ui.adapter.DragDropItemTouchHelper
import com.github.mukiva.feature.locationmanager.ui.adapter.LocationManagerSavedAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.github.mukiva.core.ui.R as CoreUiRes

internal class SavedLocationListComponent(
    private val binding: LaySavedLocationListBinding,
    private val onEnterEditMode: (editableLocation: EditableLocation) -> Unit,
    private val onSelectEditable: (editableLocation: EditableLocation) -> Unit,
    private val onItemMove: (from: Int, to: Int) -> Unit,
    private val onRetry: () -> Unit
) : Component(), Component.IStateObserver<LocationManagerViewModel> {

    private val mTouchHelperCallback by lazy { DragDropItemTouchHelper(mAddedAdapter) }
    private val mTouchHelper by lazy { ItemTouchHelper(mTouchHelperCallback) }
    private val mItemDecorator by lazy {
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = binding.root.context.resources
                    .getDimension(CoreUiRes.dimen.def_v_padding).toInt()
            }
        }
    }

    private val mAddedAdapter by lazy {
        LocationManagerSavedAdapter(
            onEnterEditMode = onEnterEditMode,
            onSelectEditable = onSelectEditable,
            onItemMoveCallback = onItemMove
        )
    }

    override fun init() = with(binding) {
        addedList.adapter = mAddedAdapter
        mTouchHelper.attachToRecyclerView(addedList)
        addedList.addItemDecoration(mItemDecorator)
    }

    override fun subscribeOnViewModel(
        viewModel: LocationManagerViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModel.savedLocationsState
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .onEach(::onUpdate)
            .launchIn(lifecycleOwner.lifecycleScope)
    }

    private fun onUpdate(state: ISavedLocationsState) = with(binding) {
        when (state) {
            is ISavedLocationsState.Content -> setContentState(state)
            ISavedLocationsState.Error -> setErrorState(onRetry)
            ISavedLocationsState.Loading -> setLoadingState()
        }
    }

    private fun LaySavedLocationListBinding.setContentState(
        state: ISavedLocationsState.Content
    ) {
        addedEmptyView.emptyView(root.context.getString(R.string.saved_locatios_empty_view))
        addedEmptyView.root.isVisible = state.data.isEmpty()
        addedList.isVisible = state.data.isNotEmpty()
        mAddedAdapter.submitList(state.data)
    }
}

internal fun LaySavedLocationListBinding.setErrorState(onRetry: () -> Unit) {
    addedEmptyView.error(
        msg = Resources.getSystem().getString(CoreUiRes.string.error_msg),
        buttonText = Resources.getSystem().getString(CoreUiRes.string.refresh),
        onButtonClick = onRetry,
    )
}

internal fun LaySavedLocationListBinding.setLoadingState() {
    addedEmptyView.loading()
}
