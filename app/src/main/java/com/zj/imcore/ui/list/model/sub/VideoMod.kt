package com.zj.imcore.ui.list.model.sub

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.zj.im.list.views.ChatItemView
import com.zj.imcore.ui.ChatOption
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.utils.img.ImageLoaderPayLoads
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
        return ImageLoaderPayLoads.CONVERSATION_VIDEO
    }

    override fun getWidth(data: MsgInfo): Int {
        return data.file?.width ?: 0
    }

    override fun getHeight(data: MsgInfo): Int {
        return data.file?.height ?: 0
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
        val url = data.file?.url ?: ""
        val duration = data.file?.duration ?: 0L
        val imgPath = data.getOriginalPath(ImageLoaderPayLoads.CONVERSATION_VIDEO)
        val info = ConversationFileInfo(SourceType.VIDEO, url, "", duration, imgPath)
        PreviewActivity.start(context, info, null)
    }
}