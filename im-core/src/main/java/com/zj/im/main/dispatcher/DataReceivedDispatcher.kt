package com.zj.im.main.dispatcher

import com.zj.im.chat.enums.SendMsgState
import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.interfaces.ConnectCallBack
import com.zj.im.chat.modle.SocketConnInfo
import com.zj.im.chat.modle.BaseMsgInfo
import com.zj.im.utils.netUtils.NetWorkInfo
import com.zj.im.main.ChatBase
import com.zj.im.main.StatusHub
import com.zj.im.utils.Constance
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

    fun onLayerChanged(isHidden: Boolean) {
        chatBase?.onAppLayerChanged(isHidden)
    }

    fun onNetworkStateChanged(netWorkState: NetWorkInfo) {
        StatusHub.isNetWorkAccess = netWorkState == NetWorkInfo.CONNECTED
        printInFile("ChatBase.IM", "the SDK checked the network status changed form ${if (StatusHub.isNetWorkAccess) "enable" else "disable"} by net State : ${netWorkState.name}")
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
            if (connState.canConnect()) {
                it.startConnect()
            }
        }
        chatBase?.notify()?.onSocketStatusChanged(connState)
    }

    fun onSendingProgress(callId: String, progress: Int) {
        getClient("on sending progress update")?.progressUpdate(progress, callId)
    }

    fun <T> conn(connInfo: SocketConnInfo?) {
        onSocketStateChange(SocketState.CONNECTION)
        getServer("on connection call")?.connect(connInfo, object : ConnectCallBack {
            override fun onConnection(isSuccess: Boolean, throwable: Throwable?) {
                val state = if (isSuccess) SocketState.CONNECTED else SocketState.CONNECTED_ERROR
                pushData(BaseMsgInfo.connectStateChange<T>(state, Constance.CONNECT_ERROR))
            }
        })
    }
}