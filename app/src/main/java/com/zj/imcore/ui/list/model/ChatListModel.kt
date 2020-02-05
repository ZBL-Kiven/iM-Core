package com.zj.imcore.ui.list.model

import android.content.Context
import com.bumptech.glide.Glide
import com.zj.ui.img.cache.ImageCacheUtil
import com.zj.imcore.utils.img.loader.AvatarLoadUtil
import com.zj.ui.list.interfaces.BaseChatModel
import com.zj.ui.list.views.ChatItemView
import com.zj.model.Payloads
import com.zj.model.chat.MsgInfo
import com.zj.imcore.base.FCApplication
import com.zj.imcore.enums.MsgType
import com.zj.imcore.ui.list.ChatOption
import com.zj.imcore.utils.img.transactions.RoundCorner

/**
 * Created by ZJJ on 19/12/12
 *
 * extends to custom any msg item;
 * */
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
        return if (FCApplication.isSelf(data.uid)) ChatItemView.Orientation.SELF else ChatItemView.Orientation.OTHERS
    }

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {

        fun loadAvatar() {
            view.getAvatarView()?.let {
                AvatarLoadUtil(context, dpToPx(context, ChatOption.avatarWidth), dpToPx(context, ChatOption.avatarHeight), ChatOption.avatarQuality, data, ImageCacheUtil.CENTER_CROP, Payloads.MEMBERS_AVATAR).load { path ->
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
            view.getNicknameView()?.text = "${data.name}"
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