package com.mukiva.location_search.ui

import android.animation.ValueAnimator
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.updatePadding

class EmptyViewAnimator(
    private val viewGroup: ViewGroup,
    private val expandBottomPadding: Int,
    private val collapsingBottomPadding: Int
) {

    private val mAnimator = ValueAnimator.ofInt(
        collapsingBottomPadding, expandBottomPadding
    ).apply {
        duration = ANIM_DURATION
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            viewGroup.updatePadding(
                bottom = it.animatedValue as Int
            )
        }
    }

    fun expand() {
        if (viewGroup.paddingBottom == expandBottomPadding)
            return
        mAnimator.start()
    }

    fun collapse() {
        if (viewGroup.paddingBottom == collapsingBottomPadding)
            return
        mAnimator.reverse()
    }

    companion object {
        private const val ANIM_DURATION = 250L
    }
}