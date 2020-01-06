package com.zj.im.store

import com.zj.im.castNotSafety
import com.zj.im.store.interfaces.DataHandler
import com.zj.im.store.interfaces.EventCallBack

@Suppress("unused")
internal class UIMaker<DATA, R, HANDLER : DataHandler<R>>(private val handler: HANDLER, eventCall: EventCallBack<DATA, R>?) {

    private var options: Options<DATA, R, HANDLER>? = null

    init {
        options = if (eventCall == null) {
            null
        } else {
            Options.create(handler, eventCall)
        }
    }

    fun push(data: DATA) {
        if (options == null) {
            println("----- 2222 & $data")
            handler.onDataGot(castNotSafety<DATA, R>(data))
            return
        }
        options?.push(data)
    }

    fun pushAll(data: List<DATA>) {
        data.forEach { this@UIMaker.push(it) }
    }

    fun onDestroy() {
        options?.onDestroy()
    }
}