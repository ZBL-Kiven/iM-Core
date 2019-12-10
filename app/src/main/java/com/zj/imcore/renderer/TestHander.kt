package com.zj.imcore.renderer

import com.zj.im.store.interfaces.EventCallBack
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.mod.MsgReceivedInfo

class TestHandler : EventCallBack<MsgReceivedInfo, MsgInfo> {

    override fun compare(a: MsgReceivedInfo, b: MsgReceivedInfo): Int {
        return when {
            a.createTs > b.createTs -> 1
            a.createTs < b.createTs -> -1
            else -> 0
        }
    }

    override fun handle(data: MsgReceivedInfo?, theEndCall: (MsgInfo?) -> Unit) {
        Thread.sleep(80)
        val d = data?.msgInfo
        d?.createTs = data?.createTs
        d?.isSelf = data?.isSelf ?: false
        theEndCall(d)
    }
}
