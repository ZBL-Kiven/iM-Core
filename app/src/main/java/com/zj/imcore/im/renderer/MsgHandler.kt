package com.zj.imcore.im.renderer

import com.zj.im.dispatcher.DataHandler
import com.zj.model.chat.MsgInfo


class MsgHandler : DataHandler<MsgInfo, MsgInfo> {
    override fun handle(data: MsgInfo): MsgInfo {
        return data
    }
}