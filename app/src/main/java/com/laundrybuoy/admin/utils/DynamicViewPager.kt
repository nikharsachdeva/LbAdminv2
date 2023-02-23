package com.laundrybuoy.admin.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class DynamicViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    private var mCurrentView: View? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var localHeightMeasureSpec = heightMeasureSpec
        if (mCurrentView == null) {
            super.onMeasure(widthMeasureSpec, localHeightMeasureSpec)
            return
        }
        var height = 0
        mCurrentView?.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        val h = mCurrentView!!.measuredHeight
        if (h > height) height = h
        localHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, localHeightMeasureSpec)
    }

    fun measureCurrentView(currentView: View) {
        mCurrentView = currentView
        requestLayout()
    }
}