package com.zj.im.chat.hub

import android.app.Application
import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.exceptions.ChatException
import com.zj.im.chat.modle.BaseMsgInfo
import com.zj.im.chat.interfaces.SendingCallBack
import com.zj.im.main.StatusHub
import com.zj.im.main.dispatcher.DataReceivedDispatcher
import com.zj.im.utils.log.NetRecordUtils
import com.zj.im.utils.log.logger.printInFile
import com.zj.im.utils.netUtils.IConnectivityManager
import com.zj.im.utils.netUtils.NetWorkInfo

@Suppress("unused")
abstract class ServerHub<T> {

    private var connectivityManager: IConnectivityManager? = null

    open fun init(context: Application?) {
        connectivityManager = IConnectivityManager()
        connectivityManager?.init(context) { netWorkStateChanged(it) }
    }

    protected abstract fun send(params: T, callId: String, callBack: SendingCallBack): Long

    abstract fun closeSocket(case: String)

    abstract fun reConnect()

    internal fun sendToSocket(params: T, callId: String, callBack: SendingCallBack) {
        val size = send(params, callId, callBack)
        if (size > 0) NetRecordUtils.recordLastModifySendData(size)
    }

    protected fun postToClose(case: String) {
        DataReceivedDispatcher.pushData<T>(BaseMsgInfo.connectStateChange(SocketState.CONNECTED_ERROR, case))
    }

    protected fun postConnectState(state: SocketState, case: String = "") {
        DataReceivedDispatcher.pushData<T>(BaseMsgInfo.connectStateChange(state, case))
    }

    protected fun postError(case: String) {
        DataReceivedDispatcher.postError(ChatException(case))
    }

    protected fun postError(throws: Throwable) {
        DataReceivedDispatcher.postError(throws)
    }

    protected fun hasNetworkAccess(): Boolean {
        return connectivityManager?.isNetWorkActive == NetWorkInfo.CONNECTED
    }

    protected fun checkNetwork(): Boolean {
        return connectivityManager?.checkNetWorkValidate() == NetWorkInfo.CONNECTED
    }

    private fun netWorkStateChanged(state: NetWorkInfo) {
        DataReceivedDispatcher.pushData(BaseMsgInfo.networkStateChanged<T>(state))
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

    fun checkNetWork() {

    }

    val isNetWorkAccess: Boolean
        get() {
            return connectivityManager?.isNetWorkActive == NetWorkInfo.CONNECTED
        }

    fun isDataEnable(): Boolean {
        return StatusHub.curSocketState.isConnected() && isNetWorkAccess
    }

    open fun shutdown() {
        closeSocket("shutdown")
        connectivityManager?.shutDown()
    }
}