package com.zj.im.chat.hub

import com.zj.im.chat.utils.netUtils.NetWorkInfo
import com.zj.im.chat.core.DataStore
import com.zj.im.chat.enums.SendMsgState
import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.interfaces.BaseMsgInfo
import com.zj.im.chat.interfaces.ConnectCallBack
import com.zj.im.chat.interfaces.HeartBeatsCallBack
import com.zj.im.chat.interfaces.SendReplyCallBack
import com.zj.im.chat.modle.AuthBuilder
import com.zj.im.chat.modle.SocketConnInfo
import com.zj.im.main.ChatBase
import com.zj.im.sender.SendObject
import com.zj.im.sender.SendingPool
import com.zj.im.utils.Constance
import com.zj.im.utils.log.logger.e

/**
 * Created by ZJJ
 *
 * any message must be handle form here
 */
internal object EventHub {

    fun put(data: BaseMsgInfo) {
        when (data.type) {
            BaseMsgInfo.MessageHandleType.SEND_MSG -> sendMsg(data.sendObject)
            BaseMsgInfo.MessageHandleType.RECEIVED_MSG -> receivedMsg(data.data)
            BaseMsgInfo.MessageHandleType.CONNECT_TO_SERVER -> connToServer(data.connInfo)
            BaseMsgInfo.MessageHandleType.CLOSE_SOCKET -> closeSocket()
            BaseMsgInfo.MessageHandleType.SOCKET_STATE -> onSocketStateChange(data.connStateChange)
            BaseMsgInfo.MessageHandleType.HEARTBEATS_SEND -> heartbeats(data.params)
            BaseMsgInfo.MessageHandleType.AUTH_SEND -> auth(data.params, data.callId)
            BaseMsgInfo.MessageHandleType.SEND_STATE_CHANGE -> sendStateChange(data.sendingState, data.callId, data.params, data.isResend)
            BaseMsgInfo.MessageHandleType.NETWORK_STATE -> networkStateChanged(data.netWorkState)
            BaseMsgInfo.MessageHandleType.SEND_PROGRESS_CHANGED -> onSendingProgress(data.callId, data.progress)
            BaseMsgInfo.MessageHandleType.AUTH_RESPONSE -> authStatusChange(data.authStatus)
            BaseMsgInfo.MessageHandleType.HEARTBEATS_RESPONSE -> onHeartBeatsResponse()
        }
    }

    private fun closeSocket() {
        getServer("closeSocket")?.closeSocket()
    }

    private fun sendMsg(sendObject: SendObject?) {
        if (sendObject != null) SendingPool.push(sendObject)
    }

    private fun auth(params: Map<String, Any>?, callId: String) {
        val sendObject = SendObject.create(callId).putAll(params ?: return)
        getServer("auth")?.sendToSocket(sendObject, object : SendReplyCallBack {
            override fun onReply(isSuccess: Boolean, sendObject: SendObject, e: Throwable?) {
                if (!isSuccess) {
                    DataStore.put(BaseMsgInfo.sendingStateChange(SendMsgState.FAIL, callId, params, sendObject.isResend()))
                }
            }
        })
    }

    private fun sendStateChange(state: SendMsgState?, callId: String?, param: Map<String, Any>?, isResend: Boolean) {
        getClient("sendStateChange")?.setSendingState(state, callId, param, isResend)
    }

    private fun networkStateChanged(netWorkState: NetWorkInfo) {
        getClient("networkStateChanged")?.setNetworkState(netWorkState)
    }

    private fun receivedMsg(data: Map<String, Any>?) {
        getClient("receivedMsg")?.sendReceivedData(data)
    }

    private fun connToServer(connInfo: SocketConnInfo?) {
        getServer("connToServer")?.connect(connInfo?.address ?: "", connInfo?.port ?: 0, connInfo?.socketTimeOut, object : ConnectCallBack {
            override fun onConnection(isSuccess: Boolean, throwable: Throwable?) {
                val state = if (isSuccess) SocketState.CONNECTED else SocketState.CONNECTED_ERROR
                DataStore.put(BaseMsgInfo.connectStateChange(state, Constance.CONNECT_ERROR))
            }
        })
    }

    private fun heartbeats(params: Map<String, Any>?) {
        if (params != null) {
            getServer("heartbeats")?.send(params, object : HeartBeatsCallBack {
                override fun heartBeats(isOK: Boolean, throwable: Throwable?) {
                    if (isOK) getClient("heartbeats")?.nextHeartBeats()
                    else {
                        DataStore.put(BaseMsgInfo.connectStateChange(SocketState.CONNECTED_ERROR, "the heartbeats was failed to send to server"))
                    }
                }
            })
        } else {
            getClient("heartbeats")?.nextHeartBeats()
            e("ClientHub.startHeartBeats", "heart-beats was not work on this time with null params")
        }
    }

    private fun onSocketStateChange(state: SocketState?) {
        getClient("onSocketStateChange")?.changeSocketState(state ?: SocketState.INIT)
    }

    private fun onSendingProgress(callId: String, progress: Int) {
        getClient("onSendingProgress")?.onSendingProgress(callId, progress)
    }

    private fun onHeartBeatsResponse() {
        getClient("onHeartBeatsResponse")?.onHeartbeatsReceived()
    }

    private fun authStatusChange(state: AuthBuilder.AuthStatus?) {
        getClient("authStatusChange")?.authStateChange(state)
    }


    private fun getServer(where: String): ServerHub? {
        return ChatBase.getServer(where)
    }

    private fun getClient(where: String): ClientHub? {
        return ChatBase.getClient(where)
    }
}
