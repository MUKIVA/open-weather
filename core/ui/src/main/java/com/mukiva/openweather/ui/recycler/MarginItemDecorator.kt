package com.mukiva.openweather.ui.recycler

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecorator(
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

        val index = parent.indexOfChild(view)
        if (index < state.itemCount - 1)
            outRect.apply {
                top = t
                right = e
                left = s
            }
        else
            outRect.apply {
                top = t
                bottom = b
                right = e
                left = s
            }
    }


    companion object {
        fun byDirections(@Px h: Int = 0, @Px v: Int = 0): MarginItemDecorator {
            return MarginItemDecorator(
                s = h,
                e = h,
                t = v,
                b = v
            )
        }
    }

}