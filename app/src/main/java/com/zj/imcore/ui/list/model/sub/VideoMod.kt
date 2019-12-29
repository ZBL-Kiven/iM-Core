package com.zj.imcore.ui.list.model.sub

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.zj.im.list.views.ChatItemView
import com.zj.model.Payloads
import com.zj.imcore.ui.list.ChatOption
import com.zj.model.mod.MsgInfo
import com.zj.preview.PreviewActivity
import com.zj.preview.mod.ConversationFileInfo
import com.zj.preview.mod.SourceType

/**
 * Created by ZJJ on 19/12/12
 *
 * the video type of msg view model
 * */
class VideoMod : BaseImageMod() {

    override fun getDataPayloads(data: MsgInfo): String {
        return Payloads.BUBBLE_VIDEO
    }

    override fun getWidth(data: MsgInfo): Int {
        return data.impl.getVideoThumbWidth()
    }

    override fun getHeight(data: MsgInfo): Int {
        return data.impl.getVideoThumbHeight()
    }

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {
        super.initData(context, view, data, payloads)
        view.getBubbleLayout()?.let {
            val playBtnWidth = dpToPx(context, ChatOption.videoPlayViewWidth)
            val playBtnHeight = dpToPx(context, ChatOption.videoPlayViewHeight)
            val lp = FrameLayout.LayoutParams(playBtnWidth, playBtnHeight)
            lp.gravity = Gravity.CENTER
            val plv = ImageView(context)
            plv.setImageResource(ChatOption.videoPlayViewSource)
            it.addView(plv, lp)
            plv.setOnClickListener {
                openPreview(context, data)
            }
        }
    }

    private fun openPreview(context: Context, data: MsgInfo) {
        val url = data.impl.getVideoUrl() ?: ""
        val duration = data.impl.getVideoDuration()
        val imgPath = data.getOriginalPath(Payloads.BUBBLE_VIDEO) ?: ""
        val info = ConversationFileInfo(SourceType.VIDEO, url, "", duration, imgPath)
        PreviewActivity.start(context, info, null)
    }
}