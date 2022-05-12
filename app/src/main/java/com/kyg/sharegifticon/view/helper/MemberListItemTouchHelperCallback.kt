package com.kyg.sharegifticon.view.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kyg.sharegifticon.R

open class MemberListItemTouchHelperCallback(private val mContext: Context): ItemTouchHelper.Callback() {
    private val mClearPaint: Paint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    private val mBackground = ColorDrawable().apply {
        color = ContextCompat.getColor(mContext, R.color.Red)
    }
    private val deleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_remove_circle_outline_24)
    private val intrinsicWidth = deleteDrawable!!.intrinsicWidth
    private val intrinsicHeight = deleteDrawable!!.intrinsicHeight

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlag = ItemTouchHelper.LEFT
        return makeMovementFlags(0, swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val itemHeight = itemView.height

        val isCancelled = dX == 0f && !isCurrentlyActive

        if (isCancelled) {
            with(itemView) {
                clearCanvas(c, right + dX, top.toFloat(), right.toFloat(), bottom.toFloat())
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                return
            }
        }

        with(itemView) {
            mBackground.setBounds(right + dX.toInt(), top, right, bottom)
            mBackground.draw(c)
        }

        val deleteIconTop = itemView.top.plus(itemHeight.minus(intrinsicHeight) / 2)
        val deleteIconMargin = (itemHeight.minus(intrinsicHeight) / 2)
        val deleteIconLeft = itemView.right.minus(deleteIconMargin).minus(intrinsicWidth)
        val deleteIconRight = itemView.right.minus(deleteIconMargin)
        val deleteIconBottom = deleteIconTop.plus(intrinsicHeight)

        with(deleteDrawable) {
            this?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            this?.draw(c)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, mClearPaint)
    }
}