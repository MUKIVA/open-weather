package com.mukiva.feature.dashboard.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

object ScrollSynchronizer {

    private val mScrollFlow = MutableSharedFlow<Int>(
        replay = 1
    )
    private var mLastScroll = 0

    fun updateScroll(scroll: Int, scope: CoroutineScope) {
        mLastScroll = scroll
        scope.launch { mScrollFlow.emit(scroll) }
    }

    fun getOffsetFlow(): Flow<Int> = mScrollFlow

    fun getLastScroll(): Int = mLastScroll
}