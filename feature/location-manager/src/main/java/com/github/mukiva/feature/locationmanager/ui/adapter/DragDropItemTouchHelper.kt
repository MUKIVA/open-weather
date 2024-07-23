package com.github.mukiva.feature.locationmanager.ui.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

internal class DragDropItemTouchHelper(
    private val adapter: ItemTouchHelperAdapter,
) : ItemTouchHelper.Callback() {

    private var isEnabled: Boolean = true
    private var swipeIsEnabled: Boolean = false
    private var dragDropIsEnabled: Boolean = true

    override fun isLongPressDragEnabled(): Boolean = isEnabled && dragDropIsEnabled
    override fun isItemViewSwipeEnabled(): Boolean = isEnabled && swipeIsEnabled

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // do nothing
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        val dragFlags = ItemTouchHelper.DOWN or ItemTouchHelper.UP
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMove(
            viewHolder.adapterPosition,
            target.adapterPosition
        )
        return true
    }
}
