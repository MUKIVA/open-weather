package com.github.mukiva.feature.locationmanager.ui.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DragDropItemTouchHelper(
    private val adapter: ItemTouchHelperAdapter,
) : ItemTouchHelper.Callback() {

    var isEnabled: Boolean = true
    var swipeIsEnabled: Boolean = true
    var dragDropIsEnabled: Boolean = true

    override fun isLongPressDragEnabled(): Boolean = isEnabled && dragDropIsEnabled
    override fun isItemViewSwipeEnabled(): Boolean = isEnabled && swipeIsEnabled
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

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }
}
