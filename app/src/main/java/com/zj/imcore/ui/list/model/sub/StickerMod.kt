package com.zj.imcore.ui.list.model.sub

import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.zj.im.img.AutomationImageCalculateUtils
import com.zj.im.list.views.ChatItemView
import com.zj.model.Payloads
import com.zj.imcore.ui.list.ChatOption
import com.zj.imcore.R
import com.zj.model.mod.MsgInfo
import com.zj.imcore.ui.list.model.BaseItemMod

class StickerMod : BaseItemMod() {

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {
        val width = data.impl.getStickerWidth()
        val height = data.impl.getStickerHeight()
        if (width <= 0 || height <= 0) {
            view.printErrorMsg("Error print: sticker was no size")
            return
        }
        view.getBubbleLayout()?.let {
            view.removeView(it)
        }
        val nickNameId = view.getNicknameView()?.id ?: R.id.im_chat_nickname
        val avatarId = view.getAvatarView()?.id ?: R.id.im_chat_avatar
        val imageView = ImageView(context)
        val calculate = calculateImgParams(context, width, height)
        val w = calculate.second[0]
        val h = calculate.second[1]
        val lp = RelativeLayout.LayoutParams(w, h)
        if (isSelf(data.impl.uid())) {
            lp.addRule(RelativeLayout.BELOW, nickNameId)
            lp.addRule(RelativeLayout.START_OF, avatarId)
        } else {
            lp.addRule(RelativeLayout.BELOW, nickNameId)
            lp.addRule(RelativeLayout.END_OF, avatarId)
        }
        val margin = dpToPx(context, 10f)
        lp.setMargins(margin, margin, margin, margin)
        view.addView(imageView, lp)
        val imgW = (w * ChatOption.STICKER_MSG_QUALITY).toInt()
        val imgH = (h * ChatOption.STICKER_MSG_QUALITY).toInt()
        Glide.with(context).load(getUrl(data)).override(imgW, imgH).error(R.mipmap.app_common_image_loading_error).into(imageView)
    }

    private fun getUrl(data: MsgInfo): String {
        return data.getOriginalPath(Payloads.BUBBLE_STICKER)?:""
    }

    private fun calculateImgParams(context: Context, width: Int, height: Int): Pair<Boolean, Array<Int>> {
        val minScale = ChatOption.STICKER_MSG_MIN_SCALE
        val maxWidth = dpToPx(context, ChatOption.STICKER_MSG_MAX_WIDTH)
        val maxHeight = dpToPx(context, ChatOption.STICKER_MSG_MAX_HEIGHT)
        val size = AutomationImageCalculateUtils.proportionalWH(width, height, maxWidth, maxHeight, minScale)
        val isCenterCrop = size[0] >= maxWidth || size[1] >= maxHeight
        return Pair(isCenterCrop, size)
    }

}