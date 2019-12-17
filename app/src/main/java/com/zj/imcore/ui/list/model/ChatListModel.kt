package com.zj.imcore.ui.list.model

import android.content.Context
import com.bumptech.glide.Glide
import com.zj.im.img.cache.ImageCacheUtil
import com.zj.imcore.utils.img.loader.AvatarLoadUtil
import com.zj.im.list.interfaces.BaseChatModel
import com.zj.im.list.views.ChatItemView
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.enums.MsgType
import com.zj.imcore.ChatOption
import com.zj.imcore.utils.img.ImageLoaderPayLoads
import com.zj.imcore.utils.img.transactions.RoundCorner

class ChatListModel : BaseChatModel<MsgInfo> {

    override fun isInitTimeStampView(data: MsgInfo): Boolean {
        return !data.timeLineString.isNullOrEmpty()
    }

    override fun isInitInfoView(data: MsgInfo): Boolean {
        return MsgType.INFO.eq(data.subType)
    }

    override fun isInitBaseBubbleView(data: MsgInfo): Boolean {
        return MsgType.isMsg(data.subType)
    }

    override fun getOrientation(data: MsgInfo): ChatItemView.Orientation {
        return if (data.isSelf()) ChatItemView.Orientation.SELF else ChatItemView.Orientation.OTHERS
    }

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {

        fun loadAvatar() {
            view.getAvatarView()?.let {
                AvatarLoadUtil(context, dpToPx(context, ChatOption.avatarWidth), dpToPx(context, ChatOption.avatarHeight), ChatOption.avatarQuality, data, ImageCacheUtil.CENTER_CROP, ImageLoaderPayLoads.AVATAR).load { path ->
                    val avatarRadius = dpToPx(context, ChatOption.avatarRadius) * 1.0f
                    val transformer = RoundCorner(context, avatarRadius, avatarRadius, avatarRadius, avatarRadius)
                    Glide.with(context).load(path).transform(transformer).into(it)
                }
            }
        }

        fun loadTimeLine() {
            view.getTimeLineView()?.text = data.timeLineString
        }

        fun loadNickName() {
            view.getNicknameView()?.text = "${data.uid}"
        }

        fun loadBubble() {
            view.getBubbleLayout()?.removeAllViews()
            ModHub.getMode(data).initData(context, view, data, payloads)
        }

        if (!payloads.isNullOrEmpty()) {
            if (payloads.contains(ModHub.REFRESH_AVATAR)) {
                loadAvatar()
            }
            if (payloads.contains(ModHub.REFRESH_TIMELINE)) {
                loadTimeLine()
            }
            if (payloads.contains(ModHub.REFRESH_NICKNAME)) {
                loadNickName()
            }
            if (payloads.contains(ModHub.REFRESH_BUBBLE)) {
                loadBubble()
            }
        } else {
            loadAvatar()
            loadTimeLine()
            loadNickName()
            loadBubble()
        }
    }

    private fun dpToPx(context: Context, dipValue: Float): Int {
        val scale = context.applicationContext.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

}