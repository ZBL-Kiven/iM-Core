package com.zj.imcore.im.renderer

import com.zj.im.store.interfaces.EventCallBack
import com.zj.imcore.base.FCApplication
import com.zj.imcore.enums.MsgSubtype
import com.zj.imcore.enums.MsgType
import com.zj.imcore.utils.unity.DateUtils
import com.zj.model.chat.MsgInfo
import com.zj.model.interfaces.MessageIn
import com.zj.model.mod.MessageBean
import java.util.*
import kotlin.random.Random

class MsgHandler : EventCallBack<MsgInfo, MsgInfo> {

    override fun handle(data: MsgInfo, completed: (MsgInfo?) -> Unit) {
        completed(data)
    }

    override fun compare(a: MsgInfo, b: MsgInfo): Int {
        return when {
            a.localCreatedTs > b.localCreatedTs -> 1
            a.localCreatedTs < b.localCreatedTs -> -1
            else -> 0
        }
    }
}