package com.zj.im.chat.hub

import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.modle.BaseMsgInfo
import com.zj.im.chat.interfaces.ConnectCallBack
import com.zj.im.chat.interfaces.SendingCallBack
import com.zj.im.chat.modle.SocketConnInfo
import com.zj.im.main.dispatcher.DataReceivedDispatcher
import com.zj.im.utils.log.NetRecordUtils
import com.zj.im.utils.log.logger.printInFile

@Suppress("unused")
abstract class ServerHub<T> {

    abstract fun init()

    abstract fun connect(connInfo: SocketConnInfo?, callBack: ConnectCallBack?)

    protected abstract fun send(params: T?, callId: String, callBack: SendingCallBack?): Long

    abstract fun closeSocket()

    internal fun sendToSocket(params: T?, callId: String, callBack: SendingCallBack?) {
        val size = send(params, callId, callBack)
        if (size > 0) NetRecordUtils.recordLastModifySendData(size)
    }

    protected fun postToClose(case: String) {
        DataReceivedDispatcher.pushData<T>(BaseMsgInfo.connectStateChange(SocketState.CONNECTED_ERROR, case))
    }

    /**
     * @param isSpecialData This message is prioritized when calculating priority and is not affected by pauses
     * */
    protected fun postReceivedMessage(data: T, isSpecialData: Boolean, size: Long) {
        NetRecordUtils.recordLastModifySendData(size)
        DataReceivedDispatcher.pushData(BaseMsgInfo.receiveMsg(data, isSpecialData))
    }

    protected fun print(where: String, case: String) {
        printInFile(where, case)
    }

    open fun shutdown() {
        closeSocket()
    }
}