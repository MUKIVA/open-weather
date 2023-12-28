package com.mukiva.openweather.ui.recycler

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView

class PaddingItemDecorator(
    @Px private val s: Int = 0,
    @Px private val e: Int = 0,
    @Px private val t: Int = 0,
    @Px private val b: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        view.updatePadding(
            right = e,
            left = s,
            top = t,
            bottom = b
        )
    }

    companion object {
        fun byDirections(@Px h: Int = 0, @Px v: Int): PaddingItemDecorator {
            return PaddingItemDecorator(
                s = h,
                e = h,
                t = v,
                b = v
            )
        }

        fun byAll(@Px value: Int): PaddingItemDecorator {
            return PaddingItemDecorator(value, value, value, value)
        }
    }

}