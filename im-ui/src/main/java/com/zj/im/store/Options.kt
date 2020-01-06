package com.zj.im.store

import com.zj.im.store.interfaces.DataHandler
import com.zj.im.store.interfaces.EventCallBack
import java.lang.ClassCastException

@Suppress("unused")
internal class Options<DATA, R, HANDLER : DataHandler<R>> private constructor(handler: HANDLER, eventCall: EventCallBack<DATA, R>) {

    companion object {
        fun <DATA, R, HANDLER : DataHandler<R>> create(handler: HANDLER, eventCall: EventCallBack<DATA, R>): Options<DATA, R, HANDLER> {
            return Options(handler, eventCall)
        }
    }

    private val store: UIStore<DATA, R, HANDLER>?

    init {
        store = UIStore(handler, eventCall)
    }

    fun push(d: DATA) {
        @Suppress("UNCHECKED_CAST") val data: DATA = d as? DATA ?: throw ClassCastException("the data can not cast to register classes !")
        println("----- 3333 & $data")
        store?.put(data)
    }

    fun pushAll(d: List<DATA>) {
        @Suppress("UNCHECKED_CAST") val data = d as? List<DATA> ?: throw ClassCastException("the data can not cast to list of register classes !")
        store?.putAll(data)
    }

    fun onDestroy() {
        store?.onDestroy()
    }
}