package com.zj.im.store.interfaces

@Suppress("unused")
abstract class DataListener<OUT> {

    /**
     * notify Subscriber refresh when the data receiving.
     *
     * @param data the data.
     */
    abstract fun onReceived(data: OUT)

    /**
     * used when it has OnSendBefore callback and onProgress called
     * */
    open fun onProgressChange(percent: Int, callId: String) {}

}