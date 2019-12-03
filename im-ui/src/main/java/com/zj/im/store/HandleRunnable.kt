package com.zj.im.store

import com.zj.im.store.interfaces.EventCallBack
import java.util.concurrent.Callable

internal class HandleRunnable<DATA, R>(private val data: DATA?, private val eventCall: EventCallBack<DATA, R>, private val theEndCall: (R?) -> Unit) : Callable<Unit> {

    override fun call() {
        eventCall.handle(data, theEndCall)
    }

}