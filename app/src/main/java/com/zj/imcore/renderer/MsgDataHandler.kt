package com.zj.imcore.renderer

import com.zj.im.chat.interfaces.AnalyzingData
import com.zj.im.store.interfaces.EventCallBack

class MsgDataHandler : EventCallBack<AnalyzingData, String> {
    override fun compare(p0: AnalyzingData, p1: AnalyzingData): Int {
        return when {
            p0.isSelf() && !p1.isSelf() -> -1
            !p0.isSelf() && p1.isSelf() -> 1
            else -> 0
        }
    }

    override fun handle(data: AnalyzingData?, theEndCall: (String?) -> Unit) {
        Thread.sleep(80)
        theEndCall("s= $data")
    }
}
