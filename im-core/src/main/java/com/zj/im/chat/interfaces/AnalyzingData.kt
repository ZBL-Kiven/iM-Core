package com.zj.im.chat.interfaces

import com.zj.im.chat.enums.SendMsgState

/**
 * Created by ZJJ
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class AnalyzingData {

    val state: SendMsgState?
    val callId: String?

    private val type: Type
    private val param: MutableMap<String, Any>?
    private val response: MutableMap<String, Any>?

    fun getData(): MutableMap<String, Any>? {
        return if (isSelf()) param else response
    }

    fun isSelf(): Boolean {
        return type != Type.SOURCE_RECEIVE
    }

    fun isRecent(): Boolean {
        return type == Type.SOURCE_RESEND
    }
    /**
     * inner params
     * */

    internal enum class Type {

        SOURCE_SEND, SOURCE_RECEIVE, SOURCE_RESEND

    }

    /**
     * the message by send
     * */
    internal constructor(state: SendMsgState?, callId: String?, param: Map<String, Any>?, isResend: Boolean) {
        this.state = state
        this.callId = callId
        this.param = param?.toMutableMap()
        this.response = null
        this.type = if (isResend) Type.SOURCE_RESEND else Type.SOURCE_SEND
    }

    /**
     * the message form server
     * */
    internal constructor(response: Map<String, Any>?) {
        this.state = SendMsgState.SUCCESS
        this.callId = ""
        this.param = null
        this.response = response?.toMutableMap()
        this.type = Type.SOURCE_RECEIVE
    }
}