package com.zj.im.list.items

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.zj.im.R
import java.lang.IllegalArgumentException

class ChatItemView(context: Context) : RelativeLayout(context) {

    companion object {
        const val ORIENTATION_SELF = 0
        const val ORIENTATION_OTHERS = 1
    }

    private var ivAvatar: ImageView? = null
    private var bubbleLayout: BubbleLayout? = null
    private var tvName: TextView? = null

    fun init(options: ChatItemOptions) {
        ivAvatar = ImageView(context)
        bubbleLayout = BubbleLayout(context)
        tvName = TextView(context)
        ivAvatar?.id = R.id.im_chat_avatar
        tvName?.id = R.id.im_chat_nickname
        bubbleLayout?.id = R.id.im_chat_bubble
        val look = options.getLook()
        bubbleLayout?.bubbleColor = getColor(options.getBubbleColor())
        bubbleLayout?.bubbleRadius = options.getBubbleRadius()
        bubbleLayout?.shadowColor = getColor(options.getShadowColor())
        bubbleLayout?.shadowRadius = options.getShadowRadius()
        bubbleLayout?.shadowX = options.getShadowX()
        bubbleLayout?.shadowY = options.getShadowY()
        bubbleLayout?.bubblePadding = options.getBubblePadding()
        bubbleLayout?.look = look
        bubbleLayout?.lookLength = options.getLookLength()
        bubbleLayout?.lookPosition = options.getLookPosition()
        bubbleLayout?.lookWidth = options.getLookWidth()
        bubbleLayout?.postInvalidate()
        val ori = if (look == BubbleLayout.Look.LEFT) ORIENTATION_OTHERS else ORIENTATION_SELF
        reRelyViews(ori, options)
    }

    fun getAvatarView(): ImageView? {
        return ivAvatar
    }

    fun getBubbleLayout(): BubbleLayout? {
        return bubbleLayout
    }

    fun getNicknameView(): TextView? {
        return tvName
    }

    private fun reRelyViews(ori: Int, options: ChatItemOptions) {
        removeAllViews()
        val lp = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.setMargins(options.getItemMargins(), options.getItemMargins(), options.getItemMargins(), options.getItemMargins())
        val avatarLp = LayoutParams(options.getAvatarWidth(), options.getAvatarHeight())
        val bubbleLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val nicknameLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        when (ori) {
            ORIENTATION_OTHERS -> {
                avatarLp.addRule(ALIGN_PARENT_START)
                nicknameLp.addRule(END_OF, R.id.im_chat_avatar)
                bubbleLp.addRule(END_OF, R.id.im_chat_avatar)
                bubbleLp.addRule(BELOW, R.id.im_chat_nickname)
                nicknameLp.marginStart = options.getNicknameStartMargins()
                bubbleLp.marginStart = options.getBubbleStartMargins()
            }
            ORIENTATION_SELF -> {
                avatarLp.addRule(ALIGN_PARENT_END)
                nicknameLp.addRule(START_OF, R.id.im_chat_avatar)
                bubbleLp.addRule(START_OF, R.id.im_chat_avatar)
                bubbleLp.addRule(BELOW, R.id.im_chat_nickname)
                nicknameLp.marginEnd = options.getNicknameStartMargins()
                bubbleLp.marginEnd = options.getBubbleStartMargins()
            }
        }
        lp.setMargins(options.getItemMargins(), options.getItemMargins(), options.getItemMargins(), options.getItemMargins())
        bubbleLp.topMargin = options.getBubbleTopMargins()
        layoutParams = lp
        ivAvatar?.scaleType = options.getAvatarScaleType()
        tvName?.textSize = options.getNicknameTextSize()
        tvName?.setTextColor(getColor(options.getNicknameTextColor()))
        checkoutOrAdd(ivAvatar, avatarLp, this)
        checkoutOrAdd(bubbleLayout, bubbleLp, this)
        checkoutOrAdd(tvName, nicknameLp, this)
    }

    private fun checkoutOrAdd(v: View?, lp: LayoutParams, parent: ViewGroup) {
        if (v?.parent != null) {
            (v.parent as? ViewGroup)?.removeView(v)
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
}
