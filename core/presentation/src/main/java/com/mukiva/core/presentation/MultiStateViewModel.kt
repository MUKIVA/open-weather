package com.mukiva.core.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class MultiStateViewModel<T : Any> : ViewModel() {

    private val mStates = HashMap<KClass<*>, MutableStateFlow<T>>()

    fun <R : T> observeState(clazz: KClass<R>, lifecycleOwner: LifecycleOwner, observer: (R) -> Unit) {
        viewModelScope.launch {
            mStates[clazz]!!
                .asStateFlow()
                .collect {
                    if (lifecycleOwner.lifecycle.currentState != Lifecycle.State.DESTROYED)
                        observer(it as R)
                }
        }
    }
    protected fun <R : T> getState(clazz: KClass<R>): R {
        return mStates[clazz]!!.value as R
    }
    protected fun <R : T> initState(clazz: KClass<R>, state: R) {

        if (mStates.containsKey(clazz))
            error("The state has already been initialized")

        mStates[clazz] = MutableStateFlow(state)

    }
    protected fun <R : T> modifyState(clazz: KClass<R>, transform: suspend R.() -> R) {
        viewModelScope.launch {
            modifyState(clazz, (mStates[clazz]!!.value as R).transform())
        }
    }
    private fun <R : T> modifyState(clazz: KClass<R>, state: R) {
        mStates[clazz]!!.value = state
    }
}