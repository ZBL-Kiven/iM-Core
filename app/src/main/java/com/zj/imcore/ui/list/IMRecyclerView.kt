package com.zj.imcore.ui.list

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.zj.im.list.interfaces.BaseChatModel
import com.zj.im.list.ChatRecyclerView
import com.zj.im.list.utils.TimeLineInflateModel
import com.zj.im.list.ChatItemOptions
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.ui.model.ChatListModel

class IMRecyclerView @JvmOverloads constructor(context: Context, attr: AttributeSet? = null, defStyle: Int = 0) : ChatRecyclerView<MsgInfo>(context, attr, defStyle) {

    override fun createOptions(data: MsgInfo): ChatItemOptions {
        return initOptions(data)
    }

    override fun getViewModel(data: MsgInfo): BaseChatModel<MsgInfo> {
        return ChatListModel()
    }

    override fun onBuildData(data: MutableList<MsgInfo>?): MutableList<MsgInfo>? {
        if (data.isNullOrEmpty()) return null
        var lastTimeStamp = 0L
        for (m in data) {
            val curStamp = m.localCreatedTs
            val timeLineString = TimeLineInflateModel.inflateTimeLine(context, curStamp, lastTimeStamp, 4)
            m.timeLineString = timeLineString
            lastTimeStamp = curStamp
        }
        return data
    }

    private fun initOptions(data: MsgInfo): ChatItemOptions {
        return object : ChatItemOptions() {

            override fun getShadowY(): Float {
                return ChatOption.mShadowY
            }

            override fun getShadowX(): Float {
                return ChatOption.mShadowX
            }

            override fun getShadowRadius(): Float {
                return ChatOption.mShadowRadius
            }

            override fun getShadowColor(): Int {
                return ChatOption.mShadowColor
            }

            override fun getBubbleColor(): Int {
                return if (data.isSelf()) ChatOption.mBubbleColorSelf else ChatOption.mBubbleColorOthers
            }

            override fun getBubbleRadius(): Float {
                return ChatOption.mBubbleRadius
            }

            override fun getBubblePadding(): Float {
                return ChatOption.mBubblePadding
            }

            override fun getLookLength(): Float {
                return ChatOption.mLookLength
            }

            override fun getLookWidth(): Float {
                return ChatOption.mLookWidth
            }

            override fun getLookPosition(): Float {
                return ChatOption.mLookPosition
            }

            override fun getAvatarWidth(): Float {
                return ChatOption.avatarWidth
            }

            override fun getAvatarHeight(): Float {
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

            override fun getItemMarginStart(): Float {
                return ChatOption.mItemMarginStart
            }

            override fun getItemMarginEnd(): Float {
                return ChatOption.mItemMarginEnd
            }

            override fun getItemMarginTop(): Float {
                return ChatOption.mItemMarginTop
            }

            override fun getItemMarginBottom(): Float {
                return ChatOption.mItemMarginBottom
            }

            override fun getNicknameStartMargins(): Float {
                return ChatOption.mNicknameStartMargins
            }

            override fun getBubbleStartMargins(): Float {
                return ChatOption.mBubbleStartMargins
            }

            override fun getBubbleTopMargins(): Float {
                return ChatOption.mBubbleTopMargins
            }

            override fun getTimeLineTextSize(): Float {
                return ChatOption.mTimeLineTextSize
            }

            override fun getTimeLineTextColor(): Int {
                return ChatOption.mTimeLineTextColor
            }

            override fun getTimeLineTopMargin(): Float {
                return ChatOption.mTimeLineTopMargin
            }

            override fun getTimeLineBottomMargin(): Float {
                return ChatOption.mTimeLineBottomMargin
            }

            override fun getInfoLineTextSize(): Float {
                return ChatOption.mInfoLineTextSize
            }

            override fun getInfoLineTextColor(): Int {
                return ChatOption.mInfoLineTextColor
            }

            override fun getInfoLineTopMargin(): Float {
                return ChatOption.mInfoLineTopMargin
            }

            override fun getInfoLineBottomMargin(): Float {
                return ChatOption.mInfoLineBottomMargin
            }

            override fun getMaximumDiffDisplayTime(): Long {
                return ChatOption.mMaximumDiffDisplayTime
            }
        }
    }
}
