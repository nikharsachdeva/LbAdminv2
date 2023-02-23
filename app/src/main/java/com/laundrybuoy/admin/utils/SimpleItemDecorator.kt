package com.laundrybuoy.admin.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SimpleItemDecorator : RecyclerView.ItemDecoration {

    private var top_bottom: Int = 0
    private var left_right: Int = 0

    /**
     * @param top_bottom for top and bottom margin
     * @param left_right for left and right margin
     */
    constructor(top_bottom: Int, left_right: Int = 0) {
        this.top_bottom = top_bottom
        this.left_right = left_right
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = top_bottom
        outRect.top = top_bottom
        outRect.right = left_right
        outRect.left = left_right
    }
}