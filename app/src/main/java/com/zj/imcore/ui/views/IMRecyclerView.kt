package com.zj.imcore.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.zj.im.chat.enums.SendMsgState
import com.zj.ui.list.interfaces.BaseChatModel
import com.zj.ui.list.ChatRecyclerView
import com.zj.ui.list.utils.TimeLineInflateModel
import com.zj.ui.list.ChatItemOptions
import com.zj.model.chat.MsgInfo
import com.zj.imcore.base.FCApplication.Companion.isSelf
import com.zj.imcore.ui.list.ChatOption
import com.zj.imcore.ui.list.model.ChatListModel

/**
 * Created by ZJJ on 19/12/12
 *
 * the multi adapter data custom recycler view
 *
 * based of the chat list;
 * */
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
        data.forEach { m ->
            val curStamp = m.localCreatedTs
            val timeLineString = TimeLineInflateModel.inflateTimeLine(context, curStamp, lastTimeStamp, ChatOption.maximumDiffDisplayTime)
            m.timeLineString = timeLineString
            lastTimeStamp = curStamp
        }
        return data
    }

    override fun isNeedToRefresh(d1: MsgInfo, d2: MsgInfo): Boolean {
        val notSames = d1.key != d2.key || d1.callId != d2.callId
        val quickFind = d1.sendingState != d2.sendingState || d1.deleted != d2.deleted
        if (notSames || quickFind) {
            return true
        }
        val ps = MsgInfo::class.java.declaredFields
        ps.forEach {
            it.isAccessible = true
            if (it.get(d1) != it.get(d2)) {
                return true
            }
        }
        return false
    }

    private fun initOptions(data: MsgInfo): ChatItemOptions {
        return object : ChatItemOptions() {

            override fun isPrintErrorAble(): Boolean {
                return ChatOption.isPrintErrorAble
            }

            override fun getShadowY(): Float {
                return ChatOption.shadowY
            }

            override fun getShadowX(): Float {
                return ChatOption.shadowX
            }

            override fun getShadowRadius(): Float {
                return ChatOption.shadowRadius
            }

            override fun getShadowColor(): Int {
                return ChatOption.shadowColor
            }

            override fun getBubbleColor(): Int {
                val selfColor = if (data.sendingState == SendMsgState.SENDING.type) ChatOption.bubbleColorSelfSending else ChatOption.bubbleColorSelf
                return if (isSelf(data.uid)) selfColor else ChatOption.bubbleColorOthers
            }

            override fun getBubbleRadius(): Float {
                return ChatOption.bubbleRadius
            }

            override fun getBubblePadding(): Float {
                return ChatOption.bubblePadding
            }

            override fun getLookLength(): Float {
                return ChatOption.lookLength
            }

            override fun getLookWidth(): Float {
                return ChatOption.lookWidth
            }

            override fun getLookPosition(): Float {
                return ChatOption.lookPosition
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
                return ChatOption.itemMarginStart
            }

            override fun getItemMarginEnd(): Float {
                return ChatOption.itemMarginEnd
            }

            override fun getItemMarginTop(): Float {
                return ChatOption.itemMarginTop
            }

            override fun getItemMarginBottom(): Float {
                return ChatOption.itemMarginBottom
            }

            override fun getNicknameStartMargins(): Float {
                return ChatOption.nicknameStartMargins
            }

            override fun getBubbleStartMargins(): Float {
                return ChatOption.bubbleStartMargins
            }

            override fun getBubbleTopMargins(): Float {
                return ChatOption.bubbleTopMargins
            }

            override fun getTimeLineTextSize(): Float {
                return ChatOption.timeLineTextSize
            }

            override fun getTimeLineTextColor(): Int {
                return ChatOption.timeLineTextColor
            }

            override fun getTimeLineTopMargin(): Float {
                return ChatOption.timeLineTopMargin
            }

            override fun getTimeLineBottomMargin(): Float {
                return ChatOption.timeLineBottomMargin
            }

            override fun getInfoLineTextSize(): Float {
                return ChatOption.infoLineTextSize
            }

            override fun getInfoLineTextColor(): Int {
                return ChatOption.infoLineTextColor
            }

            override fun getInfoLineTopMargin(): Float {
                return ChatOption.infoLineTopMargin
            }

            override fun getInfoLineBottomMargin(): Float {
                return ChatOption.infoLineBottomMargin
            }
        }
    }
}
