package com.zj.imcore.ui.main.conversation.helper

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@Suppress("unused")
abstract class ConversationSwapCallBack : ItemTouchHelper.Callback() {

    private var canDrag = true
    private var canSwipe = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val layoutManager = recyclerView.layoutManager
        var dragFlags = 0
        var swipeFlags = 0
        if (layoutManager is GridLayoutManager) {
            // If it is a Grid layout, you cannot slide, you can only drag up, down, left and right
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            swipeFlags = 0
        } else if (layoutManager is LinearLayoutManager) {
            // If it is a vertical Linear layout, you can drag up and down and slide left and right
            if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            } else {
                // If it is a horizontal Linear layout, you can drag left and right and slide up and down
                swipeFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                dragFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
        }
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * In this callback, if it returns true, it means that a long press drag event can be triggered, and false means it cannot.
     */
    override fun isLongPressDragEnabled(): Boolean {
        return canDrag
    }

    /**
     * In this callback, if it returns true, it means that the sliding event can be triggered, and false means it cannot.
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return canSwipe
    }

    fun setCanDrag(canDrag: Boolean) {
        this.canDrag = canDrag
    }

    fun setCanSwipe(canSwipe: Boolean) {
        this.canSwipe = canSwipe
    }
}