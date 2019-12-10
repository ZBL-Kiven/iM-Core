package com.zj.im.main

import com.zj.im.scheduler.ReceiveListener
import java.util.concurrent.ConcurrentHashMap

/**
 * the public access of ui kit
 * */
object UIHelper {

    private var listeners: ConcurrentHashMap<String, ReceiveListener<*, *>>? = null
        get() {
            if (field == null) field = ConcurrentHashMap()
            return field
        }

    internal fun registerReceivedListener(name: String, l: ReceiveListener<*, *>) {
        listeners?.let {
            synchronized(it) {
                it.put(name, l)
            }
        }
    }

    internal fun unRegisterReceivedListener(name: String) {
        listeners?.let {
            synchronized(it) {
                it.remove(name)?.onDestroy()
            }
        }
    }

    /**
     * post a data into msg processor ,
     *
     * only supported type Data or List<Data>
     *
     * */
    fun postReceiveData(data: Any?) {
        listeners?.let {
            synchronized(it) {
                it.forEach { (_, v) ->
                    v.postData(data)
                }
            }
        }
    }
}
