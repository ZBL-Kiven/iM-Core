package com.zj.imcore.ui.model

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import com.zj.im.list.interfaces.BaseChatModel
import com.zj.im.list.views.ChatItemView
import com.zj.imcore.mod.MsgInfo

class ChatListModel : BaseChatModel<MsgInfo> {

    override fun isInitTimeStampView(data: MsgInfo): Boolean {
        return !data.timeLineString.isNullOrEmpty()
    }

    override fun isInitInfoView(data: MsgInfo): Boolean {
        return !data.subType.isNullOrEmpty()
    }

    override fun isInitBaseBubbleView(data: MsgInfo): Boolean {
        return !isInitInfoView(data)
    }

    override fun getOrientation(data: MsgInfo): ChatItemView.Orientation {
        return if (data.isSelf()) ChatItemView.Orientation.SELF else ChatItemView.Orientation.OTHERS
    }

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {
        if (isInitTimeStampView(data)) {
            val tsv = view.getTimeLineView()
            tsv?.text = data.timeLineString
        }
        if (isInitInfoView(data)) {
            view.getInfoLineView()?.text = data.text
        }
        if (isInitBaseBubbleView(data)) {
            view.getBubbleLayout()?.let { p ->
                if (p.childCount > 0) p.removeAllViews()
                val tv = TextView(context)
                tv.maxWidth = 700
                tv.text = data.text
                val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                p.addView(tv, lp)
            }
            view.getAvatarView()?.setBackgroundColor(Color.GRAY)
            view.getNicknameView()?.text = "${data.uid}"
        }
    }
}