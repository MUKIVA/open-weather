package com.mukiva.feature.location_manager_impl.ui

import android.animation.ValueAnimator
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.updateLayoutParams
class VerticalVisibleAnimationWrapper(
    private val viewGroup: ViewGroup
) {
    private val hiddenAnimator get() = ValueAnimator.ofInt(viewGroup.height, HIDDEN_HEIGHT).apply {
        duration = DURATION
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            viewGroup.updateLayoutParams {
                height = it.animatedValue as Int
            }
        }
    }

    private val visibleAnimator get() = ValueAnimator.ofInt(viewGroup.height, VISIBLE_HEIGHT).apply {
        duration = DURATION
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            viewGroup.updateLayoutParams {
                height = it.animatedValue as Int
            }
        }
    }

    fun visible() {
        viewGroup.clearAnimation()
        visibleAnimator.start()
    }

    fun hidden() {
        viewGroup.clearAnimation()
        hiddenAnimator.start()
    }

    companion object {
        private const val DURATION = 500L
        private const val HIDDEN_HEIGHT = 0
        private const val VISIBLE_HEIGHT = 150
    }

}