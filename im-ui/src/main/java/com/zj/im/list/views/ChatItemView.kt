package com.zj.im.list.views

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.zj.im.R
import com.zj.im.list.ChatItemOptions

/**
 * Created by ZJJ on 19/12/11"
 *
 * custom bubble drawing view
 */
@Suppress("unused")
class ChatItemView(context: Context) : RelativeLayout(context) {

    enum class Orientation {
        SELF, OTHERS, NONE
    }

    private var ivAvatar: ImageView? = null
    private var bubbleLayout: BubbleLayout? = null
    private var tvName: TextView? = null
    private var tvTimeLine: TextView? = null
    private var tvInfoLine: TextView? = null
    private var curOrientation = Orientation.NONE

    fun getAvatarView(): ImageView? {
        return ivAvatar
    }

    fun getBubbleLayout(): BubbleLayout? {
        return bubbleLayout
    }

    fun getNicknameView(): TextView? {
        return tvName
    }

    fun getTimeLineView(): TextView? {
        return tvTimeLine
    }

    fun getInfoLineView(): TextView? {
        return tvInfoLine
    }

    fun getCurOrientation(): Orientation {
        return curOrientation
    }

    internal fun initBase(options: ChatItemOptions) {
        val lp = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val layoutMarginStart = dpToPx(options.getItemMarginStart())
        val layoutMarginEnd = dpToPx(options.getItemMarginEnd())
        val layoutMarginTop = dpToPx(options.getItemMarginTop())
        val layoutMarginBottom = dpToPx(options.getItemMarginBottom())
        lp.setMargins(layoutMarginStart, layoutMarginTop, layoutMarginEnd, layoutMarginBottom)
        layoutParams = lp
    }

    internal fun initBaseBubbleView(orientation: Orientation, options: ChatItemOptions) {
        if (ivAvatar == null) {
            ivAvatar = ImageView(context)
            ivAvatar?.id = R.id.im_chat_avatar
        }
        if (bubbleLayout == null) {
            bubbleLayout = BubbleLayout(context)
            bubbleLayout?.id = R.id.im_chat_bubble
        }
        if (tvName == null) {
            tvName = TextView(context)
            tvName?.id = R.id.im_chat_nickname
        }
        if (curOrientation != orientation) {
            curOrientation = orientation
            reRelyViews(options)
        }
        initBubble(options)
    }

    internal fun initTimeLine(options: ChatItemOptions) {
        if (tvTimeLine == null) {
            tvTimeLine = TextView(context)
            tvTimeLine?.id = R.id.im_chat_time_line
        }
        val timeLineLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        timeLineLp.addRule(ALIGN_PARENT_TOP)
        timeLineLp.addRule(CENTER_HORIZONTAL)
        timeLineLp.topMargin = dpToPx(options.getTimeLineTopMargin())
        timeLineLp.bottomMargin = dpToPx(options.getTimeLineBottomMargin())
        tvTimeLine?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, options.getTimeLineTextSize())
        tvTimeLine?.setTextColor(getColor(options.getTimeLineTextColor()))
        checkoutOrAdd(tvTimeLine, timeLineLp, this)
    }

    internal fun initInfoLine(options: ChatItemOptions) {
        if (tvInfoLine == null) {
            tvInfoLine = TextView(context)
            tvInfoLine?.id = R.id.im_chat_info_line
        }
        val infoLineLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        infoLineLp.addRule(BELOW, R.id.im_chat_time_line)
        infoLineLp.addRule(CENTER_HORIZONTAL)
        infoLineLp.topMargin = dpToPx(options.getInfoLineTopMargin())
        infoLineLp.bottomMargin = dpToPx(options.getInfoLineBottomMargin())
        tvInfoLine?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, options.getInfoLineTextSize())
        tvInfoLine?.setTextColor(getColor(options.getInfoLineTextColor()))
        checkoutOrAdd(tvInfoLine, infoLineLp, this)
    }

    internal fun removeTimeLine() {
        removeView(tvTimeLine)
        tvTimeLine = null
    }

    internal fun removeInfoLine() {
        removeView(tvInfoLine)
        tvInfoLine = null
    }

    internal fun removeBaseBubbleView() {
        removeView(ivAvatar)
        removeView(tvName)
        removeView(bubbleLayout)
        bubbleLayout?.removeAllViews()
        ivAvatar = null
        tvName = null
        bubbleLayout = null
        curOrientation = Orientation.NONE
    }

    private fun initBubble(options: ChatItemOptions) {
        bubbleLayout?.let {
            it.bubbleColor = getColor(options.getBubbleColor())
            it.shadowColor = getColor(options.getShadowColor())
            it.setBubbleRadius(options.getBubbleRadius())
            it.setShadowRadius(options.getShadowRadius())
            it.setShadowX(options.getShadowX())
            it.setShadowY(options.getShadowY())
            it.setBubblePadding(options.getBubblePadding())
            it.setLookLength(options.getLookLength())
            it.setLookPosition(options.getLookPosition())
            it.setLookWidth(options.getLookWidth())
            it.postInvalidate()
        }
    }

    private fun reRelyViews(options: ChatItemOptions) {
        val avatarWidth = dpToPx(options.getAvatarWidth())
        val avatarHeight = dpToPx(options.getAvatarHeight())
        val avatarLp = LayoutParams(avatarWidth, avatarHeight)
        val bubbleLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val nicknameLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        avatarLp.addRule(BELOW, R.id.im_chat_time_line)
        nicknameLp.addRule(BELOW, R.id.im_chat_time_line)
        when (curOrientation) {
            Orientation.OTHERS -> {
                avatarLp.addRule(ALIGN_PARENT_START)
                nicknameLp.addRule(END_OF, R.id.im_chat_avatar)
                bubbleLp.addRule(END_OF, R.id.im_chat_avatar)
                bubbleLp.addRule(BELOW, R.id.im_chat_nickname)
                nicknameLp.marginStart = dpToPx(options.getNicknameStartMargins())
                bubbleLp.marginStart = dpToPx(options.getBubbleStartMargins())
                bubbleLayout?.look = BubbleLayout.Look.LEFT
            }
            Orientation.SELF -> {
                avatarLp.addRule(ALIGN_PARENT_END)
                nicknameLp.addRule(START_OF, R.id.im_chat_avatar)
                bubbleLp.addRule(START_OF, R.id.im_chat_avatar)
                bubbleLp.addRule(BELOW, R.id.im_chat_nickname)
                nicknameLp.marginEnd = dpToPx(options.getNicknameStartMargins())
                bubbleLp.marginEnd = dpToPx(options.getBubbleStartMargins())
                bubbleLayout?.look = BubbleLayout.Look.RIGHT
            }
            else -> {
                return
            }
        }
        bubbleLp.topMargin = dpToPx(options.getBubbleTopMargins())
        ivAvatar?.scaleType = options.getAvatarScaleType()
        tvName?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, options.getNicknameTextSize())
        tvName?.setTextColor(getColor(options.getNicknameTextColor()))
        checkoutOrAdd(ivAvatar, avatarLp, this)
        checkoutOrAdd(bubbleLayout, bubbleLp, this)
        checkoutOrAdd(tvName, nicknameLp, this)
    }

    private fun checkoutOrAdd(v: View?, lp: LayoutParams, parent: ViewGroup) {
        if (v?.parent != null) {
            (v.parent as? ViewGroup)?.let {
                if (it == parent) {
                    v.layoutParams = lp
                    return
                } else {
                    it.removeView(v)
                }
            }
        }
        parent.addView(v, lp)
    }

    private fun getColor(c: Int): Int {
        return try {
            ContextCompat.getColor(context, c)
        } catch (e: Exception) {
            c
        }
    }

    private fun dpToPx(dipValue: Float): Int {
        val scale = context.applicationContext.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun clear() {
        bubbleLayout?.removeAllViews()
        tvInfoLine = null
        tvName = null
        tvTimeLine = null
        bubbleLayout = null
        ivAvatar = null
        removeAllViews()
    }
}
