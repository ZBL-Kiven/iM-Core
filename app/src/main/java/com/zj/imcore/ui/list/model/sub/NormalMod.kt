package com.zj.imcore.ui.list.model.sub

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.zj.im.list.views.ChatItemView
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.ui.ChatOption
import com.zj.imcore.ui.list.model.BaseItemMod

/**
 * Created by ZJJ on 19/12/12
 *
 * the normal/text type of msg view model
 * */
class NormalMod : BaseItemMod() {

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {
        view.getBubbleLayout()?.let { p ->
            val tv = TextView(context)
            tv.maxWidth = dpToPx(context, ChatOption.NORMAL_MSG_MAX_WIDTH)
            tv.text = data.text
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ChatOption.bubbleTextSize)
            tv.setTextColor(getColor(context, ChatOption.bubbleTextColor))

            val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val margins = dpToPx(context, 3f)
            lp.setMargins(margins, margins, margins, margins)
            p.addView(tv, lp)
        }
    }
}