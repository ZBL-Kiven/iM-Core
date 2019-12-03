package com.zj.im.store.interfaces

/**
 * the data have sending to subscriber after filter and unique,
 * it may post to onReceived ， override it to made another logic.
 *
 **/
interface EventCallBack<DATA, R> {

    fun handle(data: DATA?, theEndCall: (R?) -> Unit)

    fun compare(a: DATA, b: DATA): Int

}