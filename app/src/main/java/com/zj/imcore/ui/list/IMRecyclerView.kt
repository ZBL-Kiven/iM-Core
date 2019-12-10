package com.zj.imcore.ui.list

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.zj.im.list.ChatRecyclerView
import com.zj.im.list.items.BubbleLayout
import com.zj.im.list.items.ChatItemOptions
import com.zj.im.list.items.ChatItemView
import com.zj.imcore.mod.MsgInfo

class IMRecyclerView @JvmOverloads constructor(context: Context, attr: AttributeSet? = null, defStyle: Int = 0) : ChatRecyclerView<MsgInfo>(context, attr, defStyle) {

    override fun initData(itemView: ChatItemView?, data: MsgInfo, payloads: MutableList<Any>?) {
        itemView?.getBubbleLayout()?.let { p ->
            if (p.childCount > 0) p.removeAllViews()
            val tv = TextView(context)
            tv.maxWidth = 700
            tv.text = data.data
            val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            p.addView(tv, lp)
        }
        itemView?.getAvatarView()?.setBackgroundColor(Color.GRAY)
        itemView?.getNicknameView()?.text = "${data.createTs}"
    }

    override fun initOptions(data: MsgInfo): ChatItemOptions {
        return object : ChatItemOptions() {

            override fun getShadowY(): Int {
                return ChatOption.mShadowY
            }

            override fun getShadowX(): Int {
                return ChatOption.mShadowX
            }

            override fun getShadowRadius(): Int {
                return ChatOption.mShadowRadius
            }

            override fun getShadowColor(): Int {
                return ChatOption.mShadowColor
            }

            override fun getBubbleColor(): Int {
                return if (data.isSelf) ChatOption.mBubbleColorSelf else ChatOption.mBubbleColorOthers
            }

            override fun getBubbleRadius(): Int {
                return ChatOption.mBubbleRadius
            }

            override fun getBubblePadding(): Int {
                return ChatOption.mBubblePadding
            }

            override fun getLookLength(): Int {
                return ChatOption.mLookLength
            }

            override fun getLookWidth(): Int {
                return ChatOption.mLookWidth
            }

            override fun getLookPosition(): Int {
                return ChatOption.mLookPosition
            }

            override fun getAvatarWidth(): Int {
                return ChatOption.avatarWidth
            }

            override fun getAvatarHeight(): Int {
                return ChatOption.avatarHeight
            }

            override fun getAvatarScaleType(): ImageView.ScaleType {
                return ChatOption.avatarScaleType
            }

            override fun getNicknameTextSize(): Float {
                return ChatOption.nicknameTextSize
            }

            override fun getNicknameTextColor(): Int {
                return ChatOption.nicknameTextColor
            }

            override fun getItemMargins(): Int {
                return ChatOption.mItemMargins
            }

            override fun getNicknameStartMargins(): Int {
                return ChatOption.mNicknameStartMargins
            }

            override fun getBubbleStartMargins(): Int {
                return ChatOption.mBubbleStartMargins
            }

            override fun getBubbleTopMargins(): Int {
                return ChatOption.mBubbleTopMargins
            }

            override fun getLook(): BubbleLayout.Look {
                return if (data.isSelf) BubbleLayout.Look.RIGHT else BubbleLayout.Look.LEFT
            }
        }

    }
}
