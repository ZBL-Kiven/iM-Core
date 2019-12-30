package com.zj.im.chat.hub

import com.zj.im.chat.core.DataStore
import com.zj.im.chat.interfaces.BaseMsgInfo
import com.zj.im.chat.interfaces.ConnectCallBack
import com.zj.im.chat.interfaces.HeartBeatsCallBack
import com.zj.im.chat.interfaces.SendReplyCallBack
import com.zj.im.sender.SendObject
import com.zj.im.utils.log.NetRecordUtils

abstract class ServerHub<T> : BaseMessageHub() {

    abstract fun init()

    abstract fun connect(address: String?, port: Int?, socketTimeOut: Int?, callBack: ConnectCallBack?)

    abstract fun send(params: Map<String, Any>, callBack: HeartBeatsCallBack?): Long

    abstract fun shutdown()

    abstract fun closeSocket()

    internal fun sendToSocket(sendObject: SendObject, callBack: SendReplyCallBack?) {
        val rawMsg = sendObject.getParams()
        val size = send(rawMsg, object : HeartBeatsCallBack {
            override fun heartBeats(isOK: Boolean, throwable: Throwable?) {
                callBack?.onReply(isOK, sendObject, throwable)
            }
        })
        if (size > 0) NetRecordUtils.recordLastModifySendData(size)
    }

    fun postReceivedMessage(data: T, size: Long) {
        NetRecordUtils.recordLastModifySendData(size)
        DataStore.put(BaseMsgInfo.receiveMsg(data))
    }
}