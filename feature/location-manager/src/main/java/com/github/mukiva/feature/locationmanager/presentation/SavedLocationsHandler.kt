package com.github.mukiva.feature.locationmanager.presentation

import com.github.mukiva.feature.locationmanager.domain.usecase.GetAddedLocationsUseCase
import com.github.mukiva.feature.locationmanager.domain.usecase.UpdateStoredLocationsUseCase
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

class SavedLocationsHandler @Inject constructor(
    private val updateStoredLocationsUseCase: UpdateStoredLocationsUseCase,
    private val getAddedLocationsUseCase: GetAddedLocationsUseCase,
) : ISavedLocationsHandler {

    override val savedLocationsState: StateFlow<ISavedLocationsState>
        get() = mSavedLocationsState

    private val mHandlerScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val mSavedLocationsState =
        MutableStateFlow<ISavedLocationsState>(ISavedLocationsState.Loading)

    private var mLoadSavedLocations = mHandlerScope.launch {
        getAddedLocationsUseCase()
            .map(::requestAsListState)
            .collect(mSavedLocationsState::emit)
    }

    override fun enterEditMode(location: EditableLocation) {
        val state = mSavedLocationsState.value as? ISavedLocationsState.Content
            ?: return
        val newData = state.data.map { editableLocation ->
            editableLocation.copy(
                isEditable = true,
                isSelected = location.location.id == editableLocation.location.id
            )
        }
        mSavedLocationsState.update { ISavedLocationsState.Content(newData) }
    }

    override fun enterNormalMode() {
        val state = mSavedLocationsState.value as? ISavedLocationsState.Content
            ?: return
        val newData = state.data.map { editableLocation ->
            editableLocation.copy(
                isEditable = false,
                isSelected = false
            )
        }
        mSavedLocationsState.update { ISavedLocationsState.Content(newData) }
    }

    override fun removeSelectedLocations() {
        val state = mSavedLocationsState.value as? ISavedLocationsState.Content
            ?: return
        val newData = state.data.filter { editableLocation -> !editableLocation.isSelected }
        mHandlerScope.launch { updateStoredLocationsUseCase(newData) }
        mSavedLocationsState.update { ISavedLocationsState.Content(newData) }
    }
    override fun moveLocation(from: Int, to: Int) {
        val state = mSavedLocationsState.value as? ISavedLocationsState.Content
            ?: return
        val newData = state.data.swapAll(from, to)
        mHandlerScope.launch { updateStoredLocationsUseCase(newData) }
        mSavedLocationsState.update { state.copy(data = newData) }
    }
    override fun selectAllEditable() {
        val state = mSavedLocationsState.value as? ISavedLocationsState.Content
            ?: return
        val newData = state.data.map { editableLocation ->
            editableLocation.copy(
                isSelected = true
            )
        }
        mSavedLocationsState.update { ISavedLocationsState.Content(newData) }
    }

    override fun retryLoadSavedLocations() {
        mLoadSavedLocations.cancel()
        mLoadSavedLocations = getAddedLocationsUseCase()
            .map(::requestAsListState)
            .onEach(mSavedLocationsState::emit)
            .launchIn(mHandlerScope)
    }

    override fun switchEditableSelect(editableLocation: EditableLocation) {
        val state = mSavedLocationsState.value as? ISavedLocationsState.Content
            ?: return
        val newData = state.data.map { location ->
            if (editableLocation.location.id == location.location.id) {
                location.copy(
                    isSelected = !location.isSelected
                )
            } else {
                location
            }
        }
        mSavedLocationsState.update { ISavedLocationsState.Content(newData) }
    }
    private fun List<EditableLocation>.swapAll(from: Int, to: Int): List<EditableLocation> {
        if (from < to) {
            for (i in from until to) {
                Collections.swap(this, i, i + 1)
            }
        } else {
            for (i in from downTo to + 1) {
                Collections.swap(this, i, i - 1)
            }
        }
        return this
    }
    private fun requestAsListState(
        requestResult: RequestResult<List<EditableLocation>>
    ): ISavedLocationsState {
        return when (requestResult) {
            is RequestResult.Error -> ISavedLocationsState.Error
            is RequestResult.InProgress -> ISavedLocationsState.Loading
            is RequestResult.Success ->
                ISavedLocationsState.Content(requestResult.data)
        }
    }
}
