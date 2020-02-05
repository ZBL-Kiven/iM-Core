package com.zj.imcore.ui.list.model.sub

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zj.ui.img.AutomationImageCalculateUtils
import com.zj.ui.img.cache.ImageCacheUtil
import com.zj.ui.list.views.ChatItemView
import com.zj.imcore.R
import com.zj.model.chat.MsgInfo
import com.zj.imcore.ui.list.ChatOption
import com.zj.imcore.ui.list.model.BaseItemMod
import com.zj.imcore.utils.img.loader.ImageMsgLoadUtil

/**
 * Created by ZJJ on 19/12/12
 *
 * the image type of msg view model
 * */
abstract class BaseImageMod : BaseItemMod() {

    abstract fun getDataPayloads(data: MsgInfo): String

    abstract fun getWidth(data: MsgInfo): Int

    abstract fun getHeight(data: MsgInfo): Int

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {

        view.getBubbleLayout()?.let { pop ->

            val w = getWidth(data)
            val h = getHeight(data)
            if (w <= 0 || h <= 0) {
                view.printErrorMsg("Error print: why the image info width or height is 0 but type is ${getDataPayloads(data)}?")
            }
            val p = calculateImgParams(context, w, h)
            val fillType = if (p.first) {
                ImageCacheUtil.CENTER_CROP
            } else {
                ImageCacheUtil.FIT_CENTER
            }
            val width = p.second[0]
            val height = p.second[1]
            val quality = ChatOption.IMAGE_MSG_QUALITY
            ImageMsgLoadUtil(context, width, height, quality, data, fillType, getDataPayloads(data)).load { path ->
                Glide.with(context).asBitmap().load(path).override(width, height).error(R.mipmap.app_common_image_loading_error).into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        val lp = pop.layoutParams
                        lp.width = width
                        lp.height = height
                        pop.layoutParams = lp
                        pop.bubbleColor = R.color.color_primary_disable
                        val iv = ImageView(context)
                        iv.setImageDrawable(placeholder)
                        iv.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        val ivLp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                        pop.addView(iv, ivLp)
                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val lp = pop.layoutParams
                        lp.width = width
                        lp.height = height
                        pop.layoutParams = lp
                        pop.updateBackground(resource)
                    }
                })
            }
        }
    }

    private fun calculateImgParams(context: Context, width: Int, height: Int): Pair<Boolean, Array<Int>> {
        val minScale = ChatOption.IMAGE_MSG_MIN_SCALE
        val maxWidth = dpToPx(context, ChatOption.IMAGE_MSG_MAX_WIDTH)
        val maxHeight = dpToPx(context, ChatOption.IMAGE_MSG_MAX_HEIGHT)
        val size = AutomationImageCalculateUtils.proportionalWH(width, height, maxWidth, maxHeight, minScale)
        val isCenterCrop = size[0] >= maxWidth || size[1] >= maxHeight
        return Pair(isCenterCrop, size)
    }
}