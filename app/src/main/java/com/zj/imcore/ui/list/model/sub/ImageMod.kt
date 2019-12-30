package com.zj.imcore.ui.list.model.sub

import com.zj.model.Payloads
import com.zj.model.mod.MsgInfo

class ImageMod : BaseImageMod() {

    override fun getDataPayloads(data: MsgInfo): String {
        return Payloads.BUBBLE_IMAGE
    }

    override fun getWidth(data: MsgInfo): Int {
        return data.imageWidth
    }

    override fun getHeight(data: MsgInfo): Int {
        return data.imageHeight
    }
}