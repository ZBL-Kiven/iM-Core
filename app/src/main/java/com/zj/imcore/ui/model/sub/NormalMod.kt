package com.zj.imcore.ui.model.sub

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import com.zj.im.list.views.ChatItemView
import com.zj.imcore.dpToPx
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.ui.list.ChatOption
import com.zj.imcore.ui.model.BaseItemMod

class NormalMod : BaseItemMod() {

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {
        view.getBubbleLayout()?.let { p ->
            if (p.childCount > 0) p.removeAllViews()
            val tv = TextView(context)
            tv.maxWidth = dpToPx(context, ChatOption.NORMAL_MSG_MAX_WIDTH)
            tv.text = data.text
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            p.addView(tv, lp)
        }
    }
}