package com.mukiva.openweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class SingleStateViewModel<TState, TEvent>(
    initialState: TState
) : ViewModel() {

    val state: StateFlow<TState> by lazy { mState.asStateFlow() }

    private val mState = MutableStateFlow(initialState)
    private val mEvent = MutableSharedFlow<TEvent>()

    private fun modifyState(newState: TState) {
        mState.value = newState
    }

    protected fun modifyState(initializer: TState.() -> TState) {
        modifyState(mState.value.initializer())
    }

    protected suspend fun event(evt: TEvent) {
        viewModelScope.launch {
            mEvent.emit(evt)
        }
    }

    fun subscribeOnEvent(onEvent: (TEvent) -> Unit) {
        viewModelScope.launch {
            mEvent.collectLatest { event ->
                coroutineContext.ensureActive()
                onEvent(event)
            }
        }
    }

}