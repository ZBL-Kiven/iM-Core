package com.zj.im.chat.interfaces

import com.zj.im.chat.enums.SendMsgState
import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.modle.AuthBuilder
import com.zj.im.chat.modle.SocketConnInfo
import com.zj.im.chat.utils.netUtils.NetWorkInfo
import com.zj.im.sender.SendObject
import com.zj.im.sender.SendingUp

/**
 * Created by ZJJ
 */
internal class BaseMsgInfo<T> private constructor() {

    enum class MessageHandleType {
        NETWORK_STATE, RECEIVED_MSG, SOCKET_STATE, SEND_MSG, CONNECT_TO_SERVER, HEARTBEATS_SEND, AUTH_SEND, SEND_STATE_CHANGE, CLOSE_SOCKET, SEND_PROGRESS_CHANGED, AUTH_RESPONSE, HEARTBEATS_RESPONSE
    }

    var type: MessageHandleType? = null

    var data: T? = null

    var params: Map<String, Any>? = null

    var connInfo: SocketConnInfo? = null

    var connStateChange: SocketState? = null

    var netWorkState: NetWorkInfo = NetWorkInfo.UNKNOWN

    var sendingState: SendMsgState? = null

    var isResend: Boolean = false

    var sendObject: SendObject? = null
        set(value) {
            value?.setSendingUpState(when {
                value.isOverrideSendingBefore() -> SendingUp.WAIT
                value.getSendingUpState() != SendingUp.NORMAL -> SendingUp.READY
                else -> SendingUp.NORMAL
            })
            field = value
        }

    var progress: Int = 0

    var authStatus: AuthBuilder.AuthStatus? = null

    var joinInTop = false

    /**
     *the pending id for per message，
     *
     * it used in the status notification ,example 'timeout' / 'sending status changed' / 'success' /...
     *
     * the default value is uuid
     * */
    var callId: String = ""


    companion object {
        fun <T>heartBeats(params: Map<String, Any>?): BaseMsgInfo<T> {
            val baseInfo = BaseMsgInfo<T>()
            baseInfo.params = params
            baseInfo.type = MessageHandleType.HEARTBEATS_SEND
            return baseInfo
        }

        fun <T>auth(callId: String, params: Map<String, Any>?): BaseMsgInfo<T> {
            val baseInfo = BaseMsgInfo<T>()
            baseInfo.params = params
            baseInfo.callId = callId
            baseInfo.type = MessageHandleType.AUTH_SEND
            return baseInfo
        }

        fun <T>onProgressChange(progress: Int, callId: String): BaseMsgInfo<T> {
            return BaseMsgInfo<T>().apply {
                this.callId = callId
                this.progress = progress
                this.type = MessageHandleType.SEND_PROGRESS_CHANGED
            }
        }

        fun <T>sendingStateChange(state: SendMsgState?, callId: String, params: Map<String, Any>?, isResend: Boolean): BaseMsgInfo<T> {
            val baseInfo = BaseMsgInfo<T>()
            baseInfo.sendingState = state
            baseInfo.callId = callId
            baseInfo.params = params
            baseInfo.isResend = isResend
            baseInfo.type = MessageHandleType.SEND_STATE_CHANGE
            return baseInfo
        }

        fun <T>networkStateChanged(state: NetWorkInfo): BaseMsgInfo<T> {
            val baseInfo = BaseMsgInfo<T>()
            baseInfo.netWorkState = state
            baseInfo.type = MessageHandleType.NETWORK_STATE
            return baseInfo
        }

        fun <T>connectStateChange(connStateChange: SocketState, case: String = ""): BaseMsgInfo<T> {
            val baseInfo = BaseMsgInfo<T>()
            baseInfo.type = MessageHandleType.SOCKET_STATE
            baseInfo.connStateChange = connStateChange.apply {
                this.case = case
            }
            return baseInfo
        }

        fun <T>closeSocket(): BaseMsgInfo<T> {
            return BaseMsgInfo<T>().apply {
                this.type = MessageHandleType.CLOSE_SOCKET
            }
        }

        fun <T>connectToServer(connInfo: SocketConnInfo): BaseMsgInfo<T> {
            val baseInfo = BaseMsgInfo<T>()
            baseInfo.connInfo = connInfo
            baseInfo.type = MessageHandleType.CONNECT_TO_SERVER
            return baseInfo
        }

        fun <T>sendMsg(sendObject: SendObject, joinInTop: Boolean = false): BaseMsgInfo<T> {
            return BaseMsgInfo<T>().apply {
                this.joinInTop = joinInTop
                this.sendObject = sendObject
                this.callId = sendObject.getCallId()
                this.sendingState = SendMsgState.SENDING
                this.type = MessageHandleType.SEND_MSG
            }
        }

        fun <T> receiveMsg(data: T?): BaseMsgInfo<T> {
            val baseInfo = BaseMsgInfo<T>()
            baseInfo.data = data
            baseInfo.type = MessageHandleType.RECEIVED_MSG
            return baseInfo
        }

        fun <T>authResponse(authStatus: AuthBuilder.AuthStatus): BaseMsgInfo<T> {
            return BaseMsgInfo<T>().apply {
                this.authStatus = authStatus
                this.type = MessageHandleType.AUTH_RESPONSE
            }
        }

        fun <T>heartBeatsResponse(): BaseMsgInfo<T> {
            return BaseMsgInfo<T>().apply {
                this.type = MessageHandleType.HEARTBEATS_RESPONSE
            }
        }
    }
}
