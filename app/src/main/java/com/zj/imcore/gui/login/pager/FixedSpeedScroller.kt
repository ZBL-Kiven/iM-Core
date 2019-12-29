package com.zj.imcore.gui.login.pager

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

@Suppress("unused")
class FixedSpeedScroller @JvmOverloads constructor(context: Context?, interpolator: Interpolator? = null) : Scroller(context, interpolator) {
    private var mDuration = 1500

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) { // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) { // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

    /**
     * set animation time
     *
     * @param time
     */
    fun setDuration(time: Int) {
        mDuration = time
    }

    /**
     * get current animation time
     *
     * @return
     */
    fun getMDuration(): Int {
        return mDuration
    }
}