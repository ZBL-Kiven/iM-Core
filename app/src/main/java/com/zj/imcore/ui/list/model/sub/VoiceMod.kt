package com.zj.imcore.ui.list.model.sub

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.zj.im.list.views.ChatItemView
import com.zj.imcore.ChatOption
import com.zj.imcore.dpToPx
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.ui.list.model.BaseItemMod
import com.zj.imcore.ui.list.views.VoiceView
import java.text.SimpleDateFormat
import java.util.*

class VoiceMod : BaseItemMod() {

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {
        view.getBubbleLayout()?.let { p ->
            val voiceView = VoiceView(context)
            val vlp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            vlp.marginStart = dpToPx(context, 5f)
            p.addView(voiceView)
            val tv = TextView(context)
            tv.maxWidth = dpToPx(context, ChatOption.NORMAL_MSG_MAX_WIDTH)
            val time = data.voice?.duration ?: 0
            val timeStr = SimpleDateFormat("mm : ss", Locale.getDefault()).format(Date(time))
            tv.text = timeStr
            val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.marginStart = dpToPx(context, 35f)
            p.addView(tv, lp)
        }
    }
}