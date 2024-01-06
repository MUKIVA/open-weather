package com.mukiva.feature.dashboard_impl.presentation

import com.mukiva.feature.dashboard_impl.domain.model.CurrentWithLocation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSynchronizer @Inject constructor() {

    val lastData get() = mLastData

    private val mDataListeners = mutableListOf<(List<CurrentWithLocation>) -> Unit>()
    private var mLastData = emptyList<CurrentWithLocation>()

    fun submit(data: List<CurrentWithLocation>) {
        mLastData = data
        mDataListeners.forEach {
            it(data)
        }
    }

    fun addListener(listener: (List<CurrentWithLocation>) -> Unit) {
        mDataListeners.add(listener)
        listener(lastData)
    }

}