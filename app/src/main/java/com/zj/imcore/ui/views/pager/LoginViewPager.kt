package com.zj.imcore.ui.views.pager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.annotation.UiThread
import androidx.viewpager.widget.ViewPager
import java.lang.reflect.Field

class LoginViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    /**
     * NONE : no scrolling or init
     *
     * LEFT : state is left to right
     *
     * RIGHT : state is right to left
     */
    private enum class Orientation {
        NONE, LEFT, RIGHT
    }

    private var mHeight: Int = 0
    private var listener: OnPageChangeListener? = null
    private var heights: HashMap<Int, Int>? = null

    private var curOffset = 0f
    private var lastOffset = 0f
    private var vTargetH: Int = 0
    private var curOrientation = Orientation.NONE
    private var isCanScroll = false

    var isScrolling = false
    var curSelected = 0
    private var pageChangeListener: ((v: View?) -> Unit)? = null

    init {
        heights = hashMapOf()
        setPageTransformer(true, CubePageTransformer())
        object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (isScrolling && state == SCROLL_STATE_IDLE) {
                    isScrolling = false
                    curOffset = 0f
                    lastOffset = 0f
                    vTargetH = 0
                    curSelected = currentItem
                    curOrientation = Orientation.NONE
                    resetWithCur()
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset in 0f..1f) {
                    curOffset = positionOffset
                    calculatePagerHeight()
                }
            }

            override fun onPageSelected(position: Int) {
                (adapter as? LoginPagerAdapter)?.let {
                    pageChangeListener?.invoke(getChildAt(position))
                }
            }
        }.let {
            this.listener = it
            addOnPageChangeListener(it)
        }
    }

    private fun calculatePagerHeight() {
        synchronized(this) {
            if (curOffset <= 0f) return
            if (lastOffset <= 0) lastOffset = curOffset
            else {
                if (curOrientation == Orientation.NONE) when {
                    curOffset > lastOffset -> {
                        curOrientation = Orientation.LEFT
                    }
                    curOffset < lastOffset -> {
                        curOrientation = Orientation.RIGHT
                    }
                }
                lastOffset = curOffset
                val isLeft = curOrientation == Orientation.LEFT
                if (!isScrolling) {
                    isScrolling = true
                    heights?.let {
                        val cur = curSelected
                        val next = when (curOrientation) {
                            Orientation.LEFT -> cur + 1
                            Orientation.RIGHT -> cur - 1
                            Orientation.NONE -> 0
                        }
                        val vCurH = it[cur] ?: 0
                        val vNextH = it[next] ?: 0
                        vTargetH = if (isLeft) -1 * (vCurH - vNextH) else vCurH - vNextH
                    }
                }
                val offset = if (!isLeft) 1 - curOffset else curOffset
                val offsetH = (offset * vTargetH).toInt()
                val h = mHeight + if (isLeft) offsetH else -offsetH
                updateHeight(h)
            }
        }
    }

    fun initData(pageChangeListener: (View?) -> Unit, vararg data: Int) {
        this.pageChangeListener = pageChangeListener
        if (adapter == null) adapter = LoginPagerAdapter { p, v ->
            val h = if (v.height == 0) {
                v.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
                v.measuredHeight
            } else v.height
            heights?.put(p, h)
            if (p == curSelected) {
                resetWithCur()
                pageChangeListener.invoke(v)
            }
        }
        (adapter as? LoginPagerAdapter)?.let {
            it.ids = data.toList()
            it.notifyDataSetChanged()
        }
    }

    private fun resetWithCur() {
        heights?.get(curSelected)?.let {
            mHeight = it
            updateHeight(it)
        }
    }

    @UiThread
    private fun updateHeight(h: Int) {
        val lp = layoutParams
        lp.height = h
        layoutParams = lp
    }

    override fun setPageTransformer(reverseDrawingOrder: Boolean, transformer: PageTransformer?) {
        val viewpagerClass = ViewPager::class.java
        try {
            val hasTransformer = transformer != null
            val pageTransformerField = viewpagerClass.getDeclaredField("mPageTransformer")
            pageTransformerField.isAccessible = true
            pageTransformerField.set(this, transformer)
            val drawingOrderField = viewpagerClass.getDeclaredField("mDrawingOrder")
            drawingOrderField.isAccessible = true
            if (hasTransformer) {
                drawingOrderField.setInt(this, if (reverseDrawingOrder) 2 else 1)
            } else {
                drawingOrderField.setInt(this, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun controlViewPagerSpeed(DurationSwitch: Int) {
        try {
            val mField: Field = ViewPager::class.java.getDeclaredField("mScroller")
            mField.isAccessible = true
            val mScroller = FixedSpeedScroller(context, AccelerateInterpolator())
            mScroller.duration = DurationSwitch
            mField.set(this, mScroller)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isCanScroll && super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (!isCanScroll && ev?.action == MotionEvent.ACTION_DOWN) {
            performClick()
        }
        return isCanScroll && super.onTouchEvent(ev)
    }

    override fun performClick(): Boolean {
        if (!isScrolling) super.performClick()
        return false
    }

    fun destroy() {
        listener?.let {
            removeOnPageChangeListener(it)
        }
    }
}