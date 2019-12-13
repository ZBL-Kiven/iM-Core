package com.zj.imcore.ui.model

import android.content.Context
import android.graphics.Color
import com.bumptech.glide.Glide
import com.zj.im.img.cache.loader.AvatarLoadUtil
import com.zj.im.list.interfaces.BaseChatModel
import com.zj.im.list.views.ChatItemView
import com.zj.imcore.dpToPx
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.ui.enums.MsgType
import com.zj.imcore.ui.list.ChatOption

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
                AvatarLoadUtil(context, dpToPx(context, ChatOption.avatarWidth), dpToPx(context, ChatOption.avatarHeight), data, "avatar").load { path ->
                    Glide.with(context).load(path).into(it)
                }
            }
        }

        fun loadTimeLine() {
            view.getTimeLineView()?.text = data.timeLineString
        }

        fun loadNickName() {
            view.getNicknameView()?.text = "${data.uid}"
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
                ModHub.getMode(data).initData(context, view, data, payloads)
            }
        } else {
            loadAvatar()
            loadTimeLine()
            loadNickName()
            ModHub.getMode(data).initData(context, view, data, payloads)
        }
    }
}