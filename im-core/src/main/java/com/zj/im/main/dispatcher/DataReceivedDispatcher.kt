package com.zj.im.main.dispatcher

import com.zj.im.chat.enums.SendMsgState
import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.modle.BaseMsgInfo
import com.zj.im.chat.modle.IMLifecycle
import com.zj.im.utils.netUtils.NetWorkInfo
import com.zj.im.main.ChatBase
import com.zj.im.main.StatusHub
import com.zj.im.utils.cast
import com.zj.im.utils.log.logger.printInFile

internal object DataReceivedDispatcher {

    private var chatBase: ChatBase<*>? = null
    private fun getClient(case: String) = chatBase?.getClient(case)
    private fun getServer(case: String) = chatBase?.getServer(case)

    fun init(chatBase: ChatBase<*>?) {
        this.chatBase = chatBase
    }

    fun <T> pushData(data: BaseMsgInfo<T>) {
        chatBase?.enqueue(data)
    }

    fun <T> sendMsg(data: BaseMsgInfo<T>) {
        chatBase?.sendTo(data)
    }

    fun postError(throwable: Throwable) {
        chatBase?.postError(throwable)
    }

    fun onLayerChanged(isHidden: Boolean) {
        chatBase?.onAppLayerChanged(isHidden)
    }

    fun checkNetWork() {
        getServer("on app layer changed")?.checkNetWork()
    }

    fun isDataEnable(): Boolean {
        return StatusHub.curSocketState.isConnected() && isNetWorkAccess()
    }

    private fun isNetWorkAccess(): Boolean {
        return getServer("check data enable")?.isNetWorkAccess == true
    }

    fun onLifeStateChanged(lifecycle: IMLifecycle) {
        chatBase?.notify()?.onLifecycle(lifecycle)
    }

    fun onNetworkStateChanged(netWorkState: NetWorkInfo) {
        printInFile("onNetworkStateChanged", "the SDK checked the network status changed to ${if (netWorkState == NetWorkInfo.CONNECTED) "enable" else "disable"} by net State : ${netWorkState.name}")
        chatBase?.notify()?.onNetWorkStatusChanged(netWorkState)
        if ((netWorkState == NetWorkInfo.CONNECTED && StatusHub.curSocketState.canConnect()) || netWorkState == NetWorkInfo.DISCONNECTED) {
            onSocketStateChange(SocketState.NETWORK_STATE_CHANGE)
        }
    }

    fun <T> sendingStateChanged(sendingState: SendMsgState, callId: String, data: T?, isSpecialData: Boolean, resend: Boolean) {
        getClient("on sending state changed")?.onSendingStateChanged(sendingState, callId, cast(data), isSpecialData, resend)
    }

    fun <T> received(data: T?, sendingState: SendMsgState?, callId: String, isSpecialData: Boolean) {
        sendingStateChanged(sendingState ?: SendMsgState.NONE, callId, data, isSpecialData, false)
    }

    fun onSocketStateChange(connState: SocketState) {
        StatusHub.curSocketState = connState
        getClient("on socket state changed")?.let {
            chatBase?.notify()?.onSocketStatusChanged(connState)
        }
    }

    fun onSendingProgress(callId: String, progress: Int) {
        getClient("on sending progress update")?.progressUpdate(progress, callId)
    }
}