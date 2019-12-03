package com.zj.imcore.renderer

import com.zj.im.store.interfaces.EventCallBack

class TestHandler : EventCallBack<Int, String> {

    override fun compare(a: Int, b: Int): Int {
        return when {
            a > b -> 1
            a < b -> -1
            else -> 0
        }
    }

    override fun handle(data: Int?, theEndCall: (String?) -> Unit) {
        Thread.sleep(100)
        theEndCall("received $data")
    }
}
