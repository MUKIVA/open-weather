package com.mukiva.openweather.ui

import android.util.Log
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class CollapsingToolbarStateListener : AppBarLayout.OnOffsetChangedListener {

    private var mCachedState: State = State.UNKNOWN

    enum class State {
        UNKNOWN,
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        Log.d("CollapsingToolbarStateListener", "$verticalOffset")
        if (appBarLayout == null) return
        when {
            verticalOffset >= -VERTICAL_TRIGGER_OFFSET -> {
                if (mCachedState == State.EXPANDED) return
                mCachedState = State.EXPANDED
                onStateChanged(State.EXPANDED)
            }
            abs(verticalOffset) - appBarLayout.totalScrollRange >= -VERTICAL_TRIGGER_OFFSET -> {
                if (mCachedState == State.COLLAPSED) return
                mCachedState = State.COLLAPSED
                onStateChanged(State.COLLAPSED)
            }
            else -> {
                if (mCachedState == State.IDLE) return
                mCachedState = State.IDLE
                onStateChanged(State.IDLE)
            }
        }
    }

    abstract fun onStateChanged(state: State)

    companion object {
        private const val VERTICAL_TRIGGER_OFFSET = 50
    }
}