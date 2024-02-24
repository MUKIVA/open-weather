package com.mukiva.core.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
fun <T> Flow<T>.collectWithScope(scope: CoroutineScope, observer: (T) -> Unit) {
    scope.launch {
        debounce(250L)
        collect { observer(it) }
    }
}