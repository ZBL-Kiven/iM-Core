package com.zj.imcore.ui.list.model.sub

import com.zj.im_model.Payloads
import com.zj.im_model.mod.MsgInfo

class ImageMod : BaseImageMod() {

    override fun getDataPayloads(data: MsgInfo): String {
        return Payloads.CONVERSATION_IMAGE
    }

    override fun getWidth(data: MsgInfo): Int {
        return data.file?.width ?: -1
    }

    override fun getHeight(data: MsgInfo): Int {
        return data.file?.height ?: -1
    }
}