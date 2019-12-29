package com.zj.imcore.ui.list.model.sub

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.zj.im.list.views.ChatItemView
import com.zj.imcore.ui.list.ChatOption
import com.zj.imcore.R
import com.zj.model.mod.MsgInfo
import com.zj.imcore.ui.list.model.BaseItemMod
import com.zj.imcore.ui.views.VoiceView

/**
 * Created by ZJJ on 19/12/12
 *
 * the voice type of msg view model
 * */
class VoiceMod : BaseItemMod() {

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {
        view.getBubbleLayout()?.let { p ->
            val voiceParent = RelativeLayout(context)
            val rlp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val isSelf = isSelf(data.impl.uid())
            val voiceView = VoiceView(context)
            voiceView.id = R.id.im_chat_item_bubble_voice
            voiceView.setOrientation(if (isSelf) VoiceView.ORIENTATION_LEFT else VoiceView.ORIENTATION_RIGHT)
            val vlp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            vlp.marginStart = dpToPx(context, 5f)

            val tv = TextView(context)
            tv.id = R.id.im_chat_item_bubble_voice_duration
            val time = data.impl.getVoiceDuration()
            val timeStr = getVoiceTimeStr(time)
            tv.text = timeStr
            tv.maxWidth = dpToPx(context, ChatOption.NORMAL_MSG_MAX_WIDTH)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ChatOption.bubbleTextSize)
            tv.setTextColor(getColor(context, ChatOption.bubbleTextColor))

            val tlp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            tlp.marginStart = dpToPx(context, 8f)
            tlp.addRule(RelativeLayout.CENTER_VERTICAL)

            if (isSelf) {
                tlp.rightMargin = dpToPx(context, 10f)
                tlp.addRule(RelativeLayout.ALIGN_PARENT_START)
                vlp.addRule(RelativeLayout.END_OF, R.id.im_chat_item_bubble_voice_duration)
            } else {
                tlp.leftMargin = dpToPx(context, 10f)
                vlp.addRule(RelativeLayout.ALIGN_PARENT_START)
                tlp.addRule(RelativeLayout.END_OF, R.id.im_chat_item_bubble_voice)
            }
            voiceParent.addView(tv, tlp)
            voiceParent.addView(voiceView, vlp)
            p.addView(voiceParent, rlp)
        }
    }

    private fun getVoiceTimeStr(duration: Long): String {
        var seconds = duration / 1000f
        val min = seconds / 60f
        if (min > 1) {
            seconds %= 60
        }
        if (min <= 0 && seconds <= 0) return "0\'\'"
        return "${if (min.toInt() > 0) {
            "${min.toInt()}\'"
        } else ""} ${seconds.toInt()}\'\'"
    }
}