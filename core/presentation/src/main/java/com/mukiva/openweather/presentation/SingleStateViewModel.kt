package com.mukiva.openweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class SingleStateViewModel<TState, TEvent>(
    initialState: TState
) : ViewModel() {

    val state: StateFlow<TState> by lazy { mState }
    val event: SharedFlow<TEvent> by lazy { mEvent }

    private val mState = MutableStateFlow(initialState)
    private val mEvent = MutableSharedFlow<TEvent>()

    private fun modifyState(newState: TState) {
        mState.value = newState
    }

    protected fun modifyState(initializer: TState.() -> TState) {
        modifyState(mState.value.initializer())
    }

    protected fun event(evt: TEvent) {
        viewModelScope.launch {
            mEvent.emit(evt)
        }
    }

}