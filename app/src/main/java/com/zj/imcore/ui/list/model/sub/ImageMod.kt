package com.zj.imcore.ui.list.model.sub

import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.utils.img.ImageLoaderPayLoads

class ImageMod : BaseImageMod() {

    override fun getDataPayloads(data: MsgInfo): String {
        return ImageLoaderPayLoads.CONVERSATION_IMAGE
    }

    override fun getWidth(data: MsgInfo): Int {
        return data.file?.width ?: -1
    }

    override fun getHeight(data: MsgInfo): Int {
        return data.file?.height ?: -1
    }
}